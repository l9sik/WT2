package com.poluectov.criticine.webapp.controller;

import com.poluectov.criticine.webapp.ApplicationContext;
import com.poluectov.criticine.webapp.exception.RequestCorruptedException;
import com.poluectov.criticine.webapp.exception.ServletControllerNotFoundException;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class LogoutControllerCommand implements ServletCommand, ControllerCommander{
    @Override
    public void execute(ServletRequest req, ServletResponse resp) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        request.getSession().invalidate();

        response.sendRedirect(request.getContextPath() + ApplicationContext.MAIN_PAGE_ADDRESS);
    }

    @Override
    public ServletCommand getCommand(HttpServletRequest request) throws ServletControllerNotFoundException, RequestCorruptedException {
        return this;
    }
}
