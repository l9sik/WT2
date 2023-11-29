package com.poluectov.criticine.webapp.controller.moviepage;

import com.poluectov.criticine.webapp.ApplicationContext;
import com.poluectov.criticine.webapp.controller.ErrorMessage;
import com.poluectov.criticine.webapp.controller.ServletCommand;
import com.poluectov.criticine.webapp.exception.DataBaseNotAvailableException;
import com.poluectov.criticine.webapp.exception.RequestCorruptedException;
import com.poluectov.criticine.webapp.exception.ServletControllerNotFoundException;
import com.poluectov.criticine.webapp.exception.StatementNotCreatedException;
import com.poluectov.criticine.webapp.model.data.Cinema;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GetMoviePageCommand implements ServletCommand {
    @Override
    public void execute(ServletRequest req, ServletResponse resp) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        String cinemaIdString = request.getParameter("cinema");

        boolean isInt = true;

        int cinemaId = 0;
        try{
            cinemaId = Integer.parseInt(cinemaIdString);
        }catch (NullPointerException e){
            isInt = false;
        }

        if (cinemaIdString == null || !isInt){
            try {
                ApplicationContext.INSTANCE.getControllerCommander().getCommand(null).execute(request, response);
            }catch (ServletControllerNotFoundException | RequestCorruptedException e) {
                //would not throw
            }
        }else {
            List<ErrorMessage> errors = new ArrayList<>();
            Cinema cinema = null;
            try{
                 cinema = ApplicationContext.INSTANCE.getCinemaDAO().get(cinemaId);
            }catch (DataBaseNotAvailableException e){
                errors.add(new ErrorMessage(ErrorMessage.DB_CONNECTION, "Error: connection to data base"));
            }catch (StatementNotCreatedException e){
                e.printStackTrace();
                errors.add(new ErrorMessage(ErrorMessage.DB_NOT_WORKING, "Data base not working."));
            }catch (SQLException e){
                e.printStackTrace();
                errors.add(new ErrorMessage(ErrorMessage.DB_ERROR, "Data base error."));
            }

            request.setAttribute("cinema", cinema);
            request.setAttribute("errors", errors);

            RequestDispatcher dispatcher = request.getRequestDispatcher(ApplicationContext.JSP_MOVIE_PAGE);
            dispatcher.forward(request, response);
        }

    }
}
