package com.poluectov.criticine.webapp.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

import java.io.IOException;

public interface ServletCommand {

    void execute(ServletRequest request, ServletResponse response) throws ServletException, IOException;
}
