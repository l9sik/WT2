package com.poluectov.criticine.webapp.controller.moviepage;

import com.poluectov.criticine.webapp.ApplicationContext;
import com.poluectov.criticine.webapp.controller.ErrorMessage;
import com.poluectov.criticine.webapp.controller.ServletCommand;
import com.poluectov.criticine.webapp.controller.criticspage.GetCriticsCommand;
import com.poluectov.criticine.webapp.dao.mysqldao.MySQLUserCinemaReviewDao;
import com.poluectov.criticine.webapp.exception.DataBaseNotAvailableException;
import com.poluectov.criticine.webapp.exception.RequestCorruptedException;
import com.poluectov.criticine.webapp.exception.ServletControllerNotFoundException;
import com.poluectov.criticine.webapp.exception.StatementNotCreatedException;
import com.poluectov.criticine.webapp.model.data.Cinema;
import com.poluectov.criticine.webapp.model.data.CinemaType;
import com.poluectov.criticine.webapp.model.data.UserCinemaReview;
import com.poluectov.criticine.webapp.service.Filter;
import com.poluectov.criticine.webapp.service.ServiceFilter;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

public class GetMoviePageCommand implements ServletCommand {
    Logger logger = Logger.getLogger(GetMoviePageCommand.class);
    @Override
    public void execute(ServletRequest req, ServletResponse resp) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        String cinemaIdString = request.getParameter("cinema");
        String criticsPageString = request.getParameter("page");



        boolean isInt = true;

        int cinemaId = 0;
        try{
            cinemaId = Integer.parseInt(cinemaIdString);
        }catch (NullPointerException e){
            logger.info("cinemaId is null or not int");
            isInt = false;
        }
        int page = 0;
        if (criticsPageString != null){
            try{
                page = Integer.parseInt(criticsPageString);
            }catch (NumberFormatException ignored){}
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


            List<UserCinemaReview> reviews = Collections.emptyList();
            //TODO: add page counter;
            try{
                cinema = ApplicationContext.INSTANCE.getCinemaDAO().get(cinemaId);
                CinemaType type = ApplicationContext.INSTANCE.getCinemaTypeDAO().get(cinema.getCinemaTypeId());
                cinema.setCinemaType(type);
                Map<String, Object> criticsFilterMap = Map.of(MySQLUserCinemaReviewDao.FK_CINEMA_ID, cinema.getId());

                Filter criticsFilter = new ServiceFilter(criticsFilterMap, null);
                reviews = ApplicationContext.INSTANCE
                        .getCriticsListService()
                        .getCriticsList(criticsFilter, page);
            }catch (DataBaseNotAvailableException e){
                logger.error(e);
                errors.add(new ErrorMessage(ErrorMessage.DB_CONNECTION));
            }catch (StatementNotCreatedException e){
                logger.error(e);
                errors.add(new ErrorMessage(ErrorMessage.DB_NOT_WORKING));
            }catch (SQLException e){
                logger.error(e);
                errors.add(new ErrorMessage(ErrorMessage.DB_ERROR));
            }

            request.setAttribute("cinema", cinema);
            request.setAttribute("reviews", reviews);
            request.setAttribute("errors", errors);

            RequestDispatcher dispatcher = request.getRequestDispatcher(ApplicationContext.JSP_MOVIE_PAGE);
            dispatcher.forward(request, response);
        }

    }
}
