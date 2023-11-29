package com.poluectov.criticine.webapp.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class NotFoundPageCommand implements ServletCommand, ControllerCommander {
    @Override
    public void execute(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        HttpServletResponse httpResponce = (HttpServletResponse) response;
        httpResponce.setStatus(404);
        httpResponce.getWriter().println("<h1>NULL PAGE BUT IN CONTROLLER :)</h1>");
    }
    @Override
    public ServletCommand getCommand(HttpServletRequest request) {
        return this;
    }
}
