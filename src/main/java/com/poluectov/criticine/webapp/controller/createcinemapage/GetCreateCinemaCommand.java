package com.poluectov.criticine.webapp.controller.createcinemapage;

import com.poluectov.criticine.webapp.ApplicationContext;
import com.poluectov.criticine.webapp.controller.ErrorMessage;
import com.poluectov.criticine.webapp.controller.ServletCommand;
import com.poluectov.criticine.webapp.exception.DataBaseNotAvailableException;
import com.poluectov.criticine.webapp.exception.RequestCorruptedException;
import com.poluectov.criticine.webapp.exception.ServletControllerNotFoundException;
import com.poluectov.criticine.webapp.model.Log4j;
import com.poluectov.criticine.webapp.model.data.CinemaType;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class GetCreateCinemaCommand implements ServletCommand {

    private static Logger logger = Logger.getLogger(GetCreateCinemaCommand.class.getName());
    @Override
    public void execute(ServletRequest request, ServletResponse response) throws ServletException, IOException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        Object o = req.getSession().getAttribute("user_role");
        Integer role = (Integer) o;
        if (o == null || !role.equals(2)){
            try {
                ApplicationContext.INSTANCE.getControllerCommander().getCommand(null).execute(request, response);
            } catch (ServletControllerNotFoundException | RequestCorruptedException e) {
             //ignore
            }
            return;
        }

        List<ErrorMessage> errors = new ArrayList<>();
        List<CinemaType> cinemaTypes = new ArrayList<>();
        try {
            cinemaTypes = ApplicationContext.INSTANCE.getCinemaTypeDAO().getAllTypes();
        } catch (DataBaseNotAvailableException e){
            logger.error(e);
            errors.add(new ErrorMessage(ErrorMessage.DB_CONNECTION));
        } catch (SQLException e) {
            logger.error(e);
            //Log4j.getLogger().error(ErrorMessage.DB_ERROR, e);
            errors.add(new ErrorMessage(ErrorMessage.DB_ERROR));
        }
        String cinemaId = req.getParameter("cinema-id");
        String cinemaName = req.getParameter("cinema-name");
        String creationYear = req.getParameter("cinema-creation-year");


        //PUT, DELETE pages
        String method = req.getParameter("method");
        if (method != null) req.setAttribute("method", method);

        if (cinemaId != null) req.setAttribute("cinema_id", cinemaId);
        if (cinemaName != null) req.setAttribute("cinema_name", cinemaName);
        if (creationYear != null) req.setAttribute("cinema_creation_year", creationYear);

        req.setAttribute("cinemaTypes", cinemaTypes);
        req.setAttribute("errors", errors);


        RequestDispatcher dispatcher = req.getRequestDispatcher(ApplicationContext.JSP_CREATE_MOVIE_PAGE);
        dispatcher.forward(req, resp);
    }
}
