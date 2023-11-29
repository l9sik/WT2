package com.poluectov.criticine.webapp.controller;

import com.poluectov.criticine.webapp.ApplicationContext;
import com.poluectov.criticine.webapp.exception.RequestCorruptedException;
import com.poluectov.criticine.webapp.exception.ServletControllerNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.Map;

public class HttpControllerCommander implements ControllerCommander {
    Map<String, ControllerCommander> controllerFactoryMap;
    ControllerCommander notFoundPage;
    public HttpControllerCommander(ControllerCommander mainPageControllerCommander,
                                   ControllerCommander registrationControllerCommander,
                                   ControllerCommander reviewControllerCommander,
                                   ControllerCommander criticsControllerCommander,
                                   ControllerCommander createCinemaControllerCommander,
                                   ControllerCommander cinemaControllerCommander,
                                   ControllerCommander notFoundPageControllerCommander){
        controllerFactoryMap = new HashMap<>();
        notFoundPage = notFoundPageControllerCommander;

        controllerFactoryMap.put(null, mainPageControllerCommander);
        controllerFactoryMap.put(ApplicationContext.REGISTRATION_PAGE_ADDRESS, registrationControllerCommander);
        controllerFactoryMap.put(ApplicationContext.REVIEW_PAGE_ADDRESS, reviewControllerCommander);
        controllerFactoryMap.put(ApplicationContext.CRITICS_PAGE_ADDRESS, criticsControllerCommander);
        controllerFactoryMap.put(ApplicationContext.CREATE_CINEMA_ADDRESS, createCinemaControllerCommander);
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
            specificController = notFoundPage;
        }
        if (specificController == null){
            specificController = notFoundPage;
        }
        return specificController;
    }

}
