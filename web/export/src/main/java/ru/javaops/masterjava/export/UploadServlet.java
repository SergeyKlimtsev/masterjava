package ru.javaops.masterjava.export;

import com.google.common.collect.Lists;
import one.util.streamex.StreamEx;
import org.thymeleaf.context.WebContext;
import ru.javaops.masterjava.export.vo.UsersExportResultVO;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.dao.UserDao;
import ru.javaops.masterjava.persist.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static ru.javaops.masterjava.export.ThymeleafListener.engine;

@WebServlet("/")
@MultipartConfig
public class UploadServlet extends HttpServlet {

    private static final int THREAD_NUMBER = 10;

    private final UserExport userExport = new UserExport();
    private final static ExecutorService executor = Executors.newFixedThreadPool(UploadServlet.THREAD_NUMBER);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final WebContext webContext = new WebContext(req, resp, req.getServletContext(), req.getLocale());
        webContext.setVariable("hasUploads", false);
        engine.process("export", webContext, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final WebContext webContext = new WebContext(req, resp, req.getServletContext(), req.getLocale());

        try {
//            http://docs.oracle.com/javaee/6/tutorial/doc/glraq.html
            Part filePart = req.getPart("fileToUpload");
            try (InputStream is = filePart.getInputStream()) {
                UsersExportResultVO resultVO = userExport.processToResultVO(is);

                int chunkSize = Integer.parseInt(req.getParameter("chunkSize"));
                persistUsersInChunk(resultVO.getUsers(), chunkSize);
                // persistUsers(resultVO.getUsers());
                webContext.setVariable("hasUploads", true);
                webContext.setVariable("successful", resultVO.getSuccessful());
                webContext.setVariable("errors", resultVO.getErrors());
                webContext.setVariable("errorsLocations", resultVO.getErrorsLocations());
                engine.process("export", webContext, resp.getWriter());

            }
        } catch (Exception e) {
            webContext.setVariable("exception", e);
            engine.process("exception", webContext, resp.getWriter());
        }
    }

    private void persistUsers(List<User> users) {
        UserDao userDao = DBIProvider.getDao(UserDao.class);
        StreamEx
                .of(users)
                .forEach(userDao::insert);
    }

    private void persistUsersInChunk(List<User> users, int chunkSize) throws InterruptedException {
        final UserDao userDao = DBIProvider.getDao(UserDao.class);
        List<List<User>> userChunks = Lists.partition(users, chunkSize);
        List<Callable<Void>> tasks = new ArrayList<>();
        StreamEx.of(userChunks).forEach(usersList -> tasks.add(() -> {
            userDao.insertAll(usersList);
            return null;
        }));
        executor.invokeAll(tasks);
    }
}
