package com.poluectov.criticine.webapp.model;


import org.apache.log4j.Logger;

import java.util.Arrays;

public class Log4j {

    private static Logger logger = Logger.getLogger(Log4j.class);
    public static void main(String[] args) {
        Arrays.stream(Thread.currentThread().getContextClassLoader().getDefinedPackages()).forEach(System.out::println);
        logger.debug("Debug message");
        logger.info("Info message");
        logger.error("Error message");
    }
}
