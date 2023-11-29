package com.poluectov.criticine.webapp.web.servlet;

import com.poluectov.criticine.webapp.ApplicationContext;
import com.poluectov.criticine.webapp.exception.RequestCorruptedException;
import com.poluectov.criticine.webapp.exception.ServletControllerNotFoundException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/app/*")
@MultipartConfig

public class MainServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handleRequest(req, resp);

    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handleRequest(req, resp);
    }
    void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {

            ApplicationContext.INSTANCE
                    .getControllerCommander()
                    .getCommand(request)
                    .execute(request, response);
        }catch (ServletControllerNotFoundException e){
            //todo: log and exception message
        }catch (RequestCorruptedException e){
            //todo: log
        }
    }
}
