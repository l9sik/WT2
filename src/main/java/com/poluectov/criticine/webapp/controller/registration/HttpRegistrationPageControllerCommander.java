package com.poluectov.criticine.webapp.controller.registration;

import com.poluectov.criticine.webapp.controller.ControllerCommander;
import com.poluectov.criticine.webapp.controller.ServletCommand;
import com.poluectov.criticine.webapp.exception.RequestCorruptedException;
import com.poluectov.criticine.webapp.exception.ServletControllerNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

public class HttpRegistrationPageControllerCommander implements ControllerCommander {

    ServletCommand get;
    ServletCommand post;

    public HttpRegistrationPageControllerCommander(ServletCommand getController,
                                                   ServletCommand postController){
        this.get = getController;
        this.post = postController;
    }

    @Override
    public ServletCommand getCommand(HttpServletRequest request) throws ServletControllerNotFoundException,
            RequestCorruptedException {
        String method = request.getMethod();
        if (method == null){
            throw new RequestCorruptedException("Request method is NULL");
        }
        if (method.equalsIgnoreCase("GET"))
            return get;
        if (method.equalsIgnoreCase("POST"))
            return post;
        throw new ServletControllerNotFoundException("Not supported request method");
    }
}
