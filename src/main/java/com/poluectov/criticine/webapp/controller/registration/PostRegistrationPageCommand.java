package com.poluectov.criticine.webapp.controller.registration;

import com.poluectov.criticine.webapp.ApplicationContext;
import com.poluectov.criticine.webapp.controller.ErrorMessage;
import com.poluectov.criticine.webapp.controller.ServletCommand;
import com.poluectov.criticine.webapp.controller.criticspage.GetCriticsCommand;
import com.poluectov.criticine.webapp.dao.UserDAO;
import com.poluectov.criticine.webapp.exception.DataBaseNotAvailableException;
import com.poluectov.criticine.webapp.exception.StatementNotCreatedException;
import com.poluectov.criticine.webapp.model.data.User;
import com.poluectov.criticine.webapp.model.security.PasswordVerifier;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PostRegistrationPageCommand implements ServletCommand {
    Logger logger = Logger.getLogger(PostRegistrationPageCommand.class);
    @Override
    public void execute(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        boolean isRedirect = true;
        List<ErrorMessage> errors = new ArrayList<>();

        String name = req.getParameter("name").trim();
        String email = req.getParameter("email").trim();
        String password = req.getParameter("password");
        logger.info("User registration for " + name + " " + email + " " + password);
        //TODO: verify email by regular expression

        String passwordHash = PasswordVerifier.createPasswordHash(password);

        User user = new User(name, email, passwordHash);

        try{
            UserDAO userDao = ApplicationContext.INSTANCE.getUserDAO();
            userDao.create(user);
        }catch (DataBaseNotAvailableException e){
            //no internet connection
            logger.error(e);
            isRedirect = false;
            errors.add(new ErrorMessage(ErrorMessage.DB_CONNECTION));
        }catch (StatementNotCreatedException e){
            //db not working
            logger.error(e);
            isRedirect = false;
            errors.add(new ErrorMessage(ErrorMessage.DB_NOT_WORKING));
        }catch (SQLException e){
            //db don't take these data
            //maybe already exists
            logger.error(e);
            isRedirect = false;
            errors.add(new ErrorMessage(ErrorMessage.DB_DATA_ERROR));
        }

        if (isRedirect){
            resp.sendRedirect(ApplicationContext.DOMAIN_ADDRESS + ApplicationContext.MAIN_PAGE_ADDRESS);
        }else{
            req.setAttribute("errors", errors);
            logger.debug(errors);

            RequestDispatcher dispatcher = req.getRequestDispatcher(ApplicationContext.JSP_REGISTRATION_PAGE);
            dispatcher.forward(req, resp);
        }
    }
}
