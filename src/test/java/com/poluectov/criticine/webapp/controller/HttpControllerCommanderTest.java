package com.poluectov.criticine.webapp.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class HttpControllerCommanderTest {
    ControllerCommander controllerCommander;
    ControllerCommander mainPageControllerCommander;
    ControllerCommander registrationControllerCommander;
    ControllerCommander reviewControllerCommander;
    ControllerCommander criticsControllerCommander;
    ControllerCommander createControllerCommander;
    ControllerCommander movieControllerCommander;
    ControllerCommander notFoundPageControllerCommander;
    @BeforeAll
    void init(){
        mainPageControllerCommander = new NotFoundPageCommand();
        registrationControllerCommander = new NotFoundPageCommand();
        reviewControllerCommander = new NotFoundPageCommand();
        criticsControllerCommander = new NotFoundPageCommand();
        notFoundPageControllerCommander = new NotFoundPageCommand();
        movieControllerCommander = new NotFoundPageCommand();
        controllerCommander = new HttpControllerCommander(
                mainPageControllerCommander,
                registrationControllerCommander,
                reviewControllerCommander,
                criticsControllerCommander,
                createControllerCommander,
                movieControllerCommander,
                notFoundPageControllerCommander
        );
    }

    @Test
    void createController_emptyPathRequest_MainPageController(){
        //TODO: mock HttpServletRequest

    }

}