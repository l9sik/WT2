package com.poluectov.criticine.webapp.controller.registration;

import com.poluectov.criticine.webapp.ApplicationContext;
import com.poluectov.criticine.webapp.controller.ServletCommand;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

public class GetRegistrationPageCommand implements ServletCommand {
    @Override
    public void execute(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        RequestDispatcher dispatcher = servletRequest.getRequestDispatcher(ApplicationContext.JSP_REGISTRATION_PAGE);
        dispatcher.forward(request, response);
    }
}
