package com.poluectov.criticine.webapp.controller.registration;

import com.poluectov.criticine.webapp.ApplicationContext;
import com.poluectov.criticine.webapp.controller.ErrorMessage;
import com.poluectov.criticine.webapp.controller.ServletCommand;
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

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PostRegistrationPageCommand implements ServletCommand {
    @Override
    public void execute(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        boolean isRedirect = true;
        List<ErrorMessage> errors = new ArrayList<>();
        System.out.printf("User added: name(%s) email(%s) password(%s)\n",
                req.getParameter("name"),
                req.getParameter("email"),
                req.getParameter("password"));

        String name = req.getParameter("name").trim();
        String email = req.getParameter("email").trim();
        String password = req.getParameter("password");
        //TODO: verify email by regular expression



        //TODO: get password hash
        String passwordHash = PasswordVerifier.createPasswordHash(password);

        User user = new User(name, email, passwordHash);

        try{
            UserDAO userDao = ApplicationContext.INSTANCE.getUserDAO();
            userDao.create(user);
        }catch (DataBaseNotAvailableException e){
            //no internet connection
            isRedirect = false;
            errors.add(new ErrorMessage(ErrorMessage.DB_CONNECTION, "check your internet connection"));
        }catch (StatementNotCreatedException e){
            //db not working
            isRedirect = false;
            errors.add(new ErrorMessage(ErrorMessage.DB_NOT_WORKING, "data base not working"));
        }catch (SQLException e){
            //db don't take these data
            //maybe already exists
            isRedirect = false;
            errors.add(new ErrorMessage(ErrorMessage.DB_DATA_ERROR, "not valid data"));
        }catch (Exception e) {
            //we have a problem
            isRedirect = false;
            errors.add(new ErrorMessage("fatal_error", e.getMessage()));
        }

        if (isRedirect){
            resp.sendRedirect(ApplicationContext.DOMAIN_ADDRESS + ApplicationContext.MAIN_PAGE_ADDRESS);
        }else{
            req.setAttribute("errors", errors);
            System.out.println(errors);

            RequestDispatcher dispatcher = req.getRequestDispatcher(ApplicationContext.JSP_REGISTRATION_PAGE);
            dispatcher.forward(req, resp);
        }
    }
}
