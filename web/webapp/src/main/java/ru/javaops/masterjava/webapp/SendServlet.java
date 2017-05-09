package ru.javaops.masterjava.webapp;


import javax.mail.util.ByteArrayDataSource;

import lombok.extern.slf4j.Slf4j;
import ru.javaops.masterjava.service.mail.Addressee;
import ru.javaops.masterjava.service.mail.Attachment;
import ru.javaops.masterjava.service.mail.MailWSClient;
import ru.javaops.web.WebStateException;

import javax.activation.DataHandler;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;

@MultipartConfig
@WebServlet("/send")
@Slf4j
public class SendServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        String users = req.getParameter("users");
        String subject = req.getParameter("subject");
        String body = req.getParameter("body");
        String groupResult;
        try {
            Set<Addressee> addressees = MailWSClient.split(users);
            Attachment attachment = getAttachment(req);
            groupResult = MailWSClient.sendBulk(addressees, subject, body, Collections.singletonList(attachment)).toString();
        } catch (WebStateException e) {
            groupResult = e.toString();
        }
        resp.getWriter().write(groupResult);
    }

    private Attachment getAttachment(HttpServletRequest req) throws IOException, ServletException {
        Part filePart = req.getPart("attachment");
        if (filePart == null) {
            return null;
        }
        String name = filePart.getSubmittedFileName();
        ByteArrayDataSource dataSource = new ByteArrayDataSource(filePart.getInputStream(), filePart.getContentType());
        dataSource.setName(filePart.getName());
        DataHandler data = new DataHandler(dataSource);
        return new Attachment(name, data);
    }
}
