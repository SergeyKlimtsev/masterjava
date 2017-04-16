package ru.javaops.masterjava.export;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.dao.GroupDao;
import ru.javaops.masterjava.persist.dao.ProjectDao;
import ru.javaops.masterjava.persist.model.Group;
import ru.javaops.masterjava.persist.model.GroupType;
import ru.javaops.masterjava.persist.model.Project;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by root on 12.04.2017.
 */
@Slf4j
public class ProjectGroupImporter {
    private final GroupDao groupDao = DBIProvider.getDao(GroupDao.class);
    private final ProjectDao projectDao = DBIProvider.getDao(ProjectDao.class);

    private static final String PROJECT_TAG = "Project";
    private static final String GROUP_TAG = "Group";
    private static final String DESCRIPTION_TAG = "description";
    private static final String CITIES_TAG = "Cities";

    public Map<String, Group> process(StaxStreamProcessor processor) throws XMLStreamException {
        val groupsMap = groupDao.getAsMap();
        val projectsMap = projectDao.getAsMap();


        Project project = null;
        String element;
        while ((element = processor.doUntilAny(XMLEvent.START_ELEMENT, PROJECT_TAG, GROUP_TAG, DESCRIPTION_TAG, CITIES_TAG)) != null) {
            if (element.equals(CITIES_TAG)) break;
            if (element.equals(PROJECT_TAG)) {
                val projectName = processor.getAttribute("name");
                project = projectsMap.get(projectName);
                if (project == null) {
                    processor.doUntil(XMLEvent.START_ELEMENT, "description");
                    val projectDescription = processor.getReader().getElementText();
                    project = new Project(projectName, projectDescription);
                    // newProjects.add(project);
                    projectDao.insert(project);
                    projectsMap.put(projectName, project);
                }
            }
            if (element.equals(GROUP_TAG)) {
                val groupName = processor.getAttribute("name");
                if (!groupsMap.containsKey(groupName)) {
                    val groupType = GroupType.valueOf(processor.getAttribute("type"));
                    val group = new Group(groupName, groupType, project.getId());

                    groupDao.insert(group);
                    groupsMap.put(groupName, group);
                }
            }
        }
        return groupsMap;
    }
}
