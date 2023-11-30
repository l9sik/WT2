package com.poluectov.criticine.webapp.controller.loginpage;

import com.poluectov.criticine.webapp.ApplicationContext;
import com.poluectov.criticine.webapp.controller.ServletCommand;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

import java.io.IOException;

public class GetLoginPageCommand implements ServletCommand {
    @Override
    public void execute(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(ApplicationContext.JSP_LOGIN_PAGE);
        dispatcher.forward(request, response);
    }
}
