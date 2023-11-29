package com.poluectov.criticine.webapp.controller;

import com.poluectov.criticine.webapp.exception.RequestCorruptedException;
import com.poluectov.criticine.webapp.exception.ServletControllerNotFoundException;
import jakarta.servlet.http.HttpServletRequest;


/**
 * Abstract factory for creating controllers
 */
public interface ControllerCommander {

    ServletCommand getCommand(HttpServletRequest request) throws
            ServletControllerNotFoundException,
            RequestCorruptedException;

}
