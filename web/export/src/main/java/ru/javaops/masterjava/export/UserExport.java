package ru.javaops.masterjava.export;

import org.xml.sax.SAXException;
import ru.javaops.masterjava.export.vo.UsersExportResultVO;
import ru.javaops.masterjava.persist.model.User;
import ru.javaops.masterjava.persist.model.UserFlag;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * gkislin
 * 14.10.2016
 */
public class UserExport {

    public List<User> process(final InputStream is) throws XMLStreamException {
        final StaxStreamProcessor processor = new StaxStreamProcessor(is);
        List<User> users = new ArrayList<>();

        while (processor.doUntil(XMLEvent.START_ELEMENT, "User")) {
            final String email = processor.getAttribute("email");
            final UserFlag flag = UserFlag.valueOf(processor.getAttribute("flag"));
            final String fullName = processor.getReader().getElementText();
            final User user = new User(fullName, email, flag);
            users.add(user);
        }
        return users;
    }

    public UsersExportResultVO processToResultVO(final InputStream is) {
        UsersExportResultVO resultVO = new UsersExportResultVO();
        try {
            final StaxStreamProcessor processor = new StaxStreamProcessor(is);

            while (processor.doUntil(XMLEvent.START_ELEMENT, "User")) {
                try {
                    final String email = processor.getAttribute("email");
                    final UserFlag flag = UserFlag.valueOf(processor.getAttribute("flag"));
                    final String fullName = processor.getReader().getElementText();
                    final User user = new User(fullName, email, flag);
                    resultVO.addUser(user);
                    resultVO.incrementSuccessful();
                }
                catch (XMLStreamException e) {
                    resultVO.addErrorLocation(e.getLocation().toString());
                    resultVO.incrementErrors();
                }
            }
        } catch (XMLStreamException xmlEx) {
            xmlEx.printStackTrace();
        }
        return resultVO;
    }


}
