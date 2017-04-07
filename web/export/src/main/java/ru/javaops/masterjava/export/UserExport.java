package ru.javaops.masterjava.export;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import one.util.streamex.StreamEx;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.dao.*;
import ru.javaops.masterjava.persist.model.*;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * gkislin
 * 14.10.2016
 */
@Slf4j
public class UserExport {

    private UserDao userDao = DBIProvider.getDao(UserDao.class);
    private CityDao cityDao = DBIProvider.getDao(CityDao.class);
    private GroupDao groupDao = DBIProvider.getDao(GroupDao.class);
    private ProjectDao projectDao = DBIProvider.getDao(ProjectDao.class);
    private UserGroupRefs userGroupRefsDao = DBIProvider.getDao(UserGroupRefs.class);

    private static final int NUMBER_THREADS = 4;
    private ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_THREADS);
    private static final String USER_TAG = "User";
    private static final String CITY_TAG = "City";
    private static final String PROJECT_TAG = "Project";
    private static final String GROUP_TAG = "Group";
    private static final Map<String, City> CITIES = new ConcurrentHashMap<>();
    private static final Map<String, Group> GROUPS = new ConcurrentHashMap<>();

    private static List<Group> getGroups(String groupNames) {
        List<Group> groups = new ArrayList<>();
        if (groupNames == null) {
            return groups;
        }
        String[] names = groupNames.split(" ");
        String bp = "";

        for (int i = 0; i < names.length; i++) {
            Group group = GROUPS.get(names[i]);
            groups.add(group);
        }
        return groups;
    }

    @Value
    public static class FailedEmail {
        public String emailOrRange;
        public String reason;

        @Override
        public String toString() {
            return emailOrRange + " : " + reason;
        }
    }

    public List<FailedEmail> process(final InputStream is, int chunkSize) throws XMLStreamException {
        log.info("Start proseccing with chunkSize=" + chunkSize);
        CITIES.clear();
        GROUPS.clear();
        return new Callable<List<FailedEmail>>() {
            class ChunkFuture {
                String emailRange;
                Future<List<String>> future;

                public ChunkFuture(List<User> chunk, Future<List<String>> future) {
                    this.future = future;
                    this.emailRange = chunk.get(0).getEmail();
                    if (chunk.size() > 1) {
                        this.emailRange += '-' + chunk.get(chunk.size() - 1).getEmail();
                    }
                }
            }

            @Override
            public List<FailedEmail> call() throws XMLStreamException {
                List<ChunkFuture> futures = new ArrayList<>();

                int id = userDao.getSeqAndSkip(chunkSize);
                List<User> chunk = new ArrayList<>(chunkSize);
                final StaxStreamProcessor processor = new StaxStreamProcessor(is);

                String xmlTag;
                Project lastProject = null;
                while ((xmlTag = processor.doUntilAny(XMLEvent.START_ELEMENT, USER_TAG, CITY_TAG, PROJECT_TAG, GROUP_TAG)) != null) {
                    if (USER_TAG.equals(xmlTag)) {
                        final String cityName = processor.getAttribute("city");
                        final String email = processor.getAttribute("email");
                        final List<Group> groups = getGroups(processor.getAttribute("groupRefs"));
                        userGroupRefsDao.insertBatch(groups, id);

                        final UserFlag flag = UserFlag.valueOf(processor.getAttribute("flag"));
                        final String fullName = processor.getReader().getElementText();

                        final City city = CITIES.get(cityName);

                        final User user = new User(id++, fullName, email, flag, city, groups);
                        chunk.add(user);
                        if (chunk.size() == chunkSize) {
                            futures.add(submit(chunk));
                            chunk.clear();
                            id = userDao.getSeqAndSkip(chunkSize);
                        }
                    }

                    if (CITY_TAG.equals(xmlTag)) {
                        final String groupId = processor.getAttribute("id");
                        final String groupName = processor.getReader().getElementText();
                        City city = new City(groupId, groupName);
                        CITIES.put(city.getId(), city);
                        cityDao.insert(city);
                    }
                    if (PROJECT_TAG.equals(xmlTag)) {
                        final Project project = new Project();
                        final String projectName = processor.getAttribute("name");
                        project.setName(projectName);
                        processor.doUntil(XMLEvent.START_ELEMENT, "description");
                        final String projectDescription = processor.getReader().getElementText();
                        project.setDescription(projectDescription);
                        lastProject = project;
                        projectDao.insert(project);
                    }
                    if (GROUP_TAG.equals(xmlTag)) {
                        final String groupName = processor.getAttribute("name");
                        final GroupType groupType = GroupType.valueOf(processor.getAttribute("type"));
                        final Group group = new Group(groupName, groupType, lastProject);
                        GROUPS.put(group.getName(), group);
                        groupDao.insert(group);
                    }
                }

                if (!chunk.isEmpty()) {
                    futures.add(submit(chunk));
                }

                List<FailedEmail> failed = new ArrayList<>();
                futures.forEach(cf -> {
                    try {
                        failed.addAll(StreamEx.of(cf.future.get()).map(email -> new FailedEmail(email, "already present")).toList());
                        log.info(cf.emailRange + " successfully executed");
                    } catch (Exception e) {
                        log.error(cf.emailRange + " failed", e);
                        failed.add(new FailedEmail(cf.emailRange, e.toString()));
                    }
                });
                return failed;
            }

            private ChunkFuture submit(List<User> chunk) {
                ChunkFuture chunkFuture = new ChunkFuture(chunk,
                        executorService.submit(() -> userDao.insertAndGetAlreadyPresent(chunk))
                );
                log.info("Submit " + chunkFuture.emailRange);
                return chunkFuture;
            }
        }.call();
    }
}
