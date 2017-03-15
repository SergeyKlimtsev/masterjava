package ru.javaops.masterjava;

import com.google.common.io.Resources;
import ru.javaops.masterjava.xml.schema.*;
import ru.javaops.masterjava.xml.util.JaxbParser;
import ru.javaops.masterjava.xml.util.Schemas;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

/**
 * User: gkislin
 * Date: 05.08.2015
 *
 * @link http://caloriesmng.herokuapp.com/
 * @link https://github.com/JavaOPs/topjava
 */
public class Main {
    private static final JaxbParser JAXB_PARSER = new JaxbParser(ObjectFactory.class);

    static {
        JAXB_PARSER.setSchema(Schemas.ofClasspath("payload.xsd"));
    }

    public static void main(String[] args) throws Exception {
        String projectName = args[0];
        TreeSet<String> usersNames = new TreeSet<>();
        Payload payload = JAXB_PARSER.unmarshal(
                Resources.getResource("payload.xml").openStream());
        Project project = null;
        for (Project prj : payload.getProjects().getProject()) {
            if (prj.getName().equalsIgnoreCase(projectName)) {
                project = prj;
                break;
            }
        }
        if (project == null) {
            throw new Exception("No such project name");
        }
        project.getProjectGroups().getGroupID().stream().forEach(o -> {
            ((Group) o.getValue()).getMembers().getUserID().stream().forEach(u -> {
                usersNames.add(((User) u.getValue()).getFullName());
            });
        });

        usersNames.stream().forEach(System.out::println);
    }
}
