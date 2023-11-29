package com.poluectov.criticine.webapp.controller.mainpage;

import com.poluectov.criticine.webapp.controller.ControllerCommander;
import com.poluectov.criticine.webapp.controller.ServletCommand;
import jakarta.servlet.http.HttpServletRequest;

public class HttpMainPageControllerCommander implements ControllerCommander {
    @Override
    public ServletCommand getCommand(HttpServletRequest request) {
        //TODO: implement realization for different roles
        return new GetMainPageCommand();
    }
}
