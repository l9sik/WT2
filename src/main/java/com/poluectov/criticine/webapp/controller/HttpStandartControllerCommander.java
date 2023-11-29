package com.poluectov.criticine.webapp.controller;

import com.poluectov.criticine.webapp.exception.RequestCorruptedException;
import com.poluectov.criticine.webapp.exception.ServletControllerNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

public class HttpStandartControllerCommander implements ControllerCommander {
    ServletCommand get;
    ServletCommand post;
    ServletCommand patch;
    ServletCommand delete;

    public HttpStandartControllerCommander(ServletCommand get, ServletCommand post, ServletCommand patch, ServletCommand delete) {
        this.get = get;
        this.post = post;
        this.patch = patch;
        this.delete = delete;
    }

    @Override
    public ServletCommand getCommand(HttpServletRequest request) throws ServletControllerNotFoundException, RequestCorruptedException {
        String method = request.getMethod();
        if (method == null){
            throw new RequestCorruptedException("Request method is NULL");
        }
        if (method.equalsIgnoreCase("GET")) {
            if (get == null)
                throw new ServletControllerNotFoundException("Get not supported");
            return get;
        }
        if (method.equalsIgnoreCase("POST")) {
            if (post == null)
                throw new ServletControllerNotFoundException("Post not supported");
            return post;
        }
        if (method.equalsIgnoreCase("PATCH")) {
            if (patch == null)
                throw new ServletControllerNotFoundException("Patch not supported");
            return patch;
        }
        if (method.equalsIgnoreCase("DELETE")) {
            if (delete == null)
                throw new ServletControllerNotFoundException("Delete not supported");
            return delete;
        }
        throw new ServletControllerNotFoundException("Method not supported");
    }
}
