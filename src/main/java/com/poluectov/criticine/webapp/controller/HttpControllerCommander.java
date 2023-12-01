package com.poluectov.criticine.webapp.controller;

import com.poluectov.criticine.webapp.ApplicationContext;
import com.poluectov.criticine.webapp.controller.criticspage.GetCriticsCommand;
import com.poluectov.criticine.webapp.exception.RequestCorruptedException;
import com.poluectov.criticine.webapp.exception.ServletControllerNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class HttpControllerCommander implements ControllerCommander {
    Logger logger = Logger.getLogger(HttpControllerCommander.class);
    Map<String, ControllerCommander> controllerFactoryMap;
    ControllerCommander notFoundPage;
    public HttpControllerCommander(ControllerCommander mainPageControllerCommander,
                                   ControllerCommander registrationControllerCommander,
                                   ControllerCommander loginControllerCommander,
                                   ControllerCommander reviewControllerCommander,
                                   ControllerCommander criticsControllerCommander,
                                   ControllerCommander createCinemaControllerCommander,
                                   ControllerCommander cinemaControllerCommander,
                                   ControllerCommander logoutControllerCommander,
                                   ControllerCommander notFoundPageControllerCommander){
        controllerFactoryMap = new HashMap<>();
        notFoundPage = notFoundPageControllerCommander;

        controllerFactoryMap.put(null, mainPageControllerCommander);
        controllerFactoryMap.put(ApplicationContext.REGISTRATION_PAGE_ADDRESS, registrationControllerCommander);
        controllerFactoryMap.put(ApplicationContext.LOGIN_PAGE_ADDRESS, loginControllerCommander);
        controllerFactoryMap.put(ApplicationContext.REVIEW_PAGE_ADDRESS, reviewControllerCommander);
        controllerFactoryMap.put(ApplicationContext.CRITICS_PAGE_ADDRESS, criticsControllerCommander);
        controllerFactoryMap.put(ApplicationContext.CREATE_CINEMA_ADDRESS, createCinemaControllerCommander);
        controllerFactoryMap.put(ApplicationContext.LOGOUT_PAGE_ADDRESS, logoutControllerCommander);
        controllerFactoryMap.put(ApplicationContext.CINEMA_ADDRESS, cinemaControllerCommander);
    }
    @Override
    public ServletCommand getCommand(HttpServletRequest request) throws RequestCorruptedException,
            ServletControllerNotFoundException {
        return getControllerFactory(request).getCommand(request);
    }
    private ControllerCommander getControllerFactory(HttpServletRequest request){
        ControllerCommander specificController;
        try {
            String requestAddress = request.getPathInfo();
            System.out.println(requestAddress);
            specificController = controllerFactoryMap.get(requestAddress);
        }catch (NullPointerException e){
            logger.error(e);
            specificController = notFoundPage;
        }
        if (specificController == null){
            specificController = notFoundPage;
        }
        return specificController;
    }

}
