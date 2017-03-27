package ru.javaops.masterjava.export;

import org.thymeleaf.context.WebContext;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.dao.UserDao;
import ru.javaops.masterjava.persist.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static ru.javaops.masterjava.export.ThymeleafListener.engine;

/**
 * Created by root on 25.03.2017.
 */
@WebServlet({"/users", "/users/clean"})
public class UsersServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final WebContext webContext = new WebContext(req, resp, req.getServletContext(), req.getLocale());
        UserDao userDao = DBIProvider.getDao(UserDao.class);
        if (req.getRequestURI().endsWith("clean")) {
            userDao.clean();
        }
        List<User> users = userDao.getWithLimit(20);
        webContext.setVariable("users", users);
        engine.process("result", webContext, resp.getWriter());
    }
}
