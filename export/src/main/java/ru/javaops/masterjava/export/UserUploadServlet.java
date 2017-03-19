package ru.javaops.masterjava.export;

import com.google.common.base.Splitter;
import one.util.streamex.StreamEx;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import ru.javaops.masterjava.common.xml.schema.User;
import ru.javaops.masterjava.common.xml.util.StaxStreamProcessor;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.events.XMLEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.Strings.nullToEmpty;

/**
 * Created by root on 18.03.2017.
 */
public class UserUploadServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/uploadPage.jsp").forward(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!ServletFileUpload.isMultipartContent(req)) {
            PrintWriter writer = resp.getWriter();
            writer.println("Error: Form must has enctype=multipart/form-data.");
            writer.flush();
            return;
        }

        // Create a factory for disk-based file items
        DiskFileItemFactory factory = new DiskFileItemFactory();

        // Configure a repository (to ensure a secure temp location is used)
        ServletContext servletContext = this.getServletConfig().getServletContext();
        File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
        factory.setRepository(repository);

        // Create a new file upload handler
        ServletFileUpload upload = new ServletFileUpload(factory);

        // Parse the request
        List<UserDTO> users = null;
        try {
            List<FileItem> items = upload.parseRequest(req);
            users = StreamEx
                    .of(items)
                    .map(this::printUsers)
                    .flatMap(List::stream)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ServletException(e);
        }
        req.setAttribute("users", users);
        req.getRequestDispatcher("users.jsp").forward(req, resp);
    }

    private List<UserDTO> printUsers(FileItem item) {
        List<UserDTO> users = new ArrayList<>();
        try (InputStream is = item.getInputStream()) {
            StaxStreamProcessor processor = new StaxStreamProcessor(is);

            while (processor.doUntil(XMLEvent.START_ELEMENT, "User")) {
                String flag = processor.getAttribute("flag");
                String email = processor.getAttribute("email");
                String userName = processor.getText();
                UserDTO user = new UserDTO(userName, flag, email);
                users.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }
}
