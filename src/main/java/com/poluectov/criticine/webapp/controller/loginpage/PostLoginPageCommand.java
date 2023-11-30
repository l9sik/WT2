package com.poluectov.criticine.webapp.controller.loginpage;

import com.poluectov.criticine.webapp.ApplicationContext;
import com.poluectov.criticine.webapp.controller.ErrorMessage;
import com.poluectov.criticine.webapp.controller.ServletCommand;
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
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PostLoginPageCommand implements ServletCommand {
    @Override
    public void execute(ServletRequest req, ServletResponse resp) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        String userName = request.getParameter("user_name");
        String password = request.getParameter("user_password");

        List<ErrorMessage> errors = new ArrayList<>();

        if (userName != null && password != null) {

            HttpSession session = request.getSession();
            if (session.getAttribute("user_name") == null){
                errors.add(new ErrorMessage("user_not_found", "User not found"));
            }
        }else {
            errors.add(new ErrorMessage("Invalid data", "Invalid user data or password"));
        }

        if (errors.isEmpty()){
            response.sendRedirect(request.getContextPath() + ApplicationContext.MAIN_PAGE_ADDRESS);
        }else{
            request.setAttribute("errors", errors);

            RequestDispatcher dispatcher = request.getRequestDispatcher(ApplicationContext.JSP_LOGIN_PAGE);
            dispatcher.forward(request, response);
        }
    }
}
