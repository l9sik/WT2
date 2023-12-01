package com.poluectov.criticine.webapp.controller;

import com.poluectov.criticine.webapp.controller.criticspage.GetCriticsCommand;
import com.poluectov.criticine.webapp.exception.RequestCorruptedException;
import com.poluectov.criticine.webapp.exception.ServletControllerNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

public class HttpStandartControllerCommander implements ControllerCommander {
    Logger logger = Logger.getLogger(HttpStandartControllerCommander.class);
    ServletCommand get;
    ServletCommand post;
    ServletCommand put;
    ServletCommand delete;

    public HttpStandartControllerCommander(ServletCommand get, ServletCommand post, ServletCommand put, ServletCommand delete) {
        this.get = get;
        this.post = post;
        this.put = put;
        this.delete = delete;
    }

    @Override
    public ServletCommand getCommand(HttpServletRequest request) throws ServletControllerNotFoundException, RequestCorruptedException {
        String method = request.getMethod();
        if (method == null){
            logger.error("method is null");
            throw new RequestCorruptedException("Request method is NULL");
        }
        if (method.equalsIgnoreCase("GET")) {
            if (get == null){
                logger.error("get is null");
                throw new ServletControllerNotFoundException("Get not supported");
            }
            return get;
        }
        if (method.equalsIgnoreCase("POST")) {
            String methodStr = request.getParameter("method");
            if (methodStr == null){
                if (post == null){
                    logger.error("post is null");
                    throw new ServletControllerNotFoundException("Post not supported");
                }
                return post;
            }else if (methodStr.equalsIgnoreCase("PUT")){
                if (put == null){
                    logger.error("put is null");
                    throw new ServletControllerNotFoundException("Put not supported");
                }
                return put;
            }else if (methodStr.equalsIgnoreCase("DELETE")) {
                if (delete == null){
                    logger.error("delete is null");
                    throw new ServletControllerNotFoundException("Delete not supported");
                }
                return delete;
            }
            if (post == null){
                logger.error("post is null");
                throw new ServletControllerNotFoundException("Post not supported");
            }
            return post;
        }
        logger.error("Method not supported " + method);
        throw new ServletControllerNotFoundException("Method not supported");
    }
}
