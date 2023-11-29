package com.poluectov.criticine.webapp.controller.createcinemapage;

import com.poluectov.criticine.webapp.ApplicationContext;
import com.poluectov.criticine.webapp.controller.ErrorMessage;
import com.poluectov.criticine.webapp.controller.ServletCommand;
import com.poluectov.criticine.webapp.exception.DataBaseNotAvailableException;
import com.poluectov.criticine.webapp.model.data.CinemaType;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GetCreateCinemaCommand implements ServletCommand {
    @Override
    public void execute(ServletRequest request, ServletResponse response) throws ServletException, IOException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        List<ErrorMessage> errors = new ArrayList<>();
        List<CinemaType> cinemaTypes = new ArrayList<>();
        try {
            cinemaTypes = ApplicationContext.INSTANCE.getCinemaTypeDAO().getAllTypes();
        } catch (DataBaseNotAvailableException e){
            errors.add(new ErrorMessage(ErrorMessage.DB_CONNECTION, "Data base not working"));
        } catch (SQLException e) {
            errors.add(new ErrorMessage(ErrorMessage.DB_ERROR, "Data base error"));
        }

        req.getSession().setAttribute("cinemaTypes", cinemaTypes);
        if (!errors.isEmpty()){
            System.out.println(errors);
            req.getSession().setAttribute("errors", errors);
        }

        RequestDispatcher dispatcher = req.getRequestDispatcher(ApplicationContext.JSP_CREATE_MOVIE_PAGE);
        dispatcher.forward(req, resp);
    }
}
