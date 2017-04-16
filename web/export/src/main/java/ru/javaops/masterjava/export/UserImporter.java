package ru.javaops.masterjava.export;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import one.util.streamex.StreamEx;
import ru.javaops.masterjava.export.PayloadImporter.FailedEmail;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.dao.UserDao;
import ru.javaops.masterjava.persist.dao.UserGroupDao;
import ru.javaops.masterjava.persist.model.*;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * gkislin
 * 14.10.2016
 */
@Slf4j
public class UserImporter {

    private static final int NUMBER_THREADS = 4;
    private final ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_THREADS);
    private final UserDao userDao = DBIProvider.getDao(UserDao.class);
    private final UserGroupDao userGroupDao = DBIProvider.getDao(UserGroupDao.class);

    public List<FailedEmail> process(StaxStreamProcessor processor, Map<String, City> cities, Map<String, Group> groups, int chunkSize) throws XMLStreamException {
        log.info("Start processing with chunkSize=" + chunkSize);

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
                List<UserGroup> userGroupsChunk = new ArrayList<>();
                List<FailedEmail> failed = new ArrayList<>();

                while (processor.doUntil(XMLEvent.START_ELEMENT, "User")) {
                    final String email = processor.getAttribute("email");
                    String cityRef = processor.getAttribute("city");
                    City city = cities.get(cityRef);
                    String groupRefs = processor.getAttribute("groupRefs");
                    List<UserGroup> userGroups = getUserGroup(groupRefs, groups, id);
                    if (city == null) {
                        failed.add(new FailedEmail(email, "City '" + cityRef + "' is not present in DB"));
                    } else if (userGroups == null || userGroups.isEmpty()) {
                        failed.add(new FailedEmail(email, "Groups '" + groupRefs + "' is not present in DB"));
                    } else {
                        final UserFlag flag = UserFlag.valueOf(processor.getAttribute("flag"));
                        final String fullName = processor.getReader().getElementText();

                        val user = new User(id++, fullName, email, flag, city.getId());
                        userGroupsChunk.addAll(userGroups);
                        chunk.add(user);
                        if (chunk.size() == chunkSize) {
                            futures.add(submit(chunk, userGroupsChunk));
                            userGroupsChunk = new ArrayList<>();
                            chunk = new ArrayList<>(chunkSize);
                            id = userDao.getSeqAndSkip(chunkSize);
                        }
                    }
                }

                if (!chunk.isEmpty()) {
                    futures.add(submit(chunk, userGroupsChunk));
                }

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

            private ChunkFuture submit(List<User> chunk, List<UserGroup> userGroupsChunk) {
                ChunkFuture chunkFuture = new ChunkFuture(chunk,
                        executorService.submit(() -> {
                            List<String> conflictEmails = userDao.insertAndGetConflictEmails(chunk);
                            userGroupDao.insertBatch(userGroupsChunk);
                            return conflictEmails;
                        })

                );
                log.info("Submit " + chunkFuture.emailRange);
                return chunkFuture;
            }
        }.call();
    }

    private List<UserGroup> getUserGroup(String groupRefs, Map<String, Group> groupMap, Integer userId) {
        Preconditions.checkNotNull(groupMap);
        if (groupRefs == null || groupRefs.isEmpty()) {
            return null;
        }
        val userGroups = new ArrayList<UserGroup>();
        val groupNames = Splitter.on(' ').split(groupRefs);
        for (String groupName : groupNames) {
            Group group = groupMap.get(groupName);
            if (group == null) {
                return null;
            }
            userGroups.add(new UserGroup(userId, group.getId()));
        }
        return userGroups;
    }
}
