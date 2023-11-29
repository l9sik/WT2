package com.poluectov.criticine.webapp.controller.criticspage;


import com.poluectov.criticine.webapp.controller.ControllerCommander;
import com.poluectov.criticine.webapp.controller.ServletCommand;
import com.poluectov.criticine.webapp.exception.RequestCorruptedException;
import com.poluectov.criticine.webapp.exception.ServletControllerNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

public class HttpCriticsPageControllerCommander implements ControllerCommander {

    ServletCommand get;

    public HttpCriticsPageControllerCommander(ServletCommand get) {
        this.get = get;
    }

    @Override
    public ServletCommand getCommand(HttpServletRequest request) throws ServletControllerNotFoundException, RequestCorruptedException {
        return get;
    }
}
