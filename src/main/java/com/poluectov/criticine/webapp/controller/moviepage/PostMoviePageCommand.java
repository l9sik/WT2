package com.poluectov.criticine.webapp.controller.moviepage;

import com.poluectov.criticine.webapp.ApplicationContext;
import com.poluectov.criticine.webapp.controller.ErrorMessage;
import com.poluectov.criticine.webapp.controller.ServletCommand;
import com.poluectov.criticine.webapp.exception.DataBaseNotAvailableException;
import com.poluectov.criticine.webapp.exception.StatementNotCreatedException;
import com.poluectov.criticine.webapp.model.data.UserCinemaReview;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PostMoviePageCommand implements ServletCommand {
    @Override
    public void execute(ServletRequest req, ServletResponse resp) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        List<ErrorMessage> errors = new ArrayList<>();
        int cinemaId = -1;
        HttpSession session = request.getSession();
        if (session.getAttribute("user_name") == null){
            errors.add(new ErrorMessage("user_not_authorized", "You should authorize first to leave feedback"));
        }else {

            String ratingStr = request.getParameter("rating");
            String cinemaIdStr = request.getParameter("cinema-id");
            String review = request.getParameter("review");
            int rating = -1;

            try{
                rating = Integer.parseInt(ratingStr);
                cinemaId = Integer.parseInt(cinemaIdStr);
            }catch (NumberFormatException e){
                errors.add(new ErrorMessage("validation_error", "Field should be a number"));
            }
            int userId = (Integer) session.getAttribute("user_id");
            if (errors.isEmpty()){
                UserCinemaReview userCinemaReview = new UserCinemaReview(userId, cinemaId, rating, review);
                try{
                    ApplicationContext.INSTANCE.getUserCinemaReviewDAO().create(userCinemaReview);
                }catch (DataBaseNotAvailableException e){
                    //no internet connection
                    errors.add(new ErrorMessage(ErrorMessage.DB_CONNECTION, "check your internet connection"));
                }catch (StatementNotCreatedException e){
                    //db not working
                    errors.add(new ErrorMessage(ErrorMessage.DB_NOT_WORKING, "data base not working"));
                }catch (SQLException e){
                    //db don't take these data
                    //maybe already exists
                    errors.add(new ErrorMessage(ErrorMessage.DB_DATA_ERROR, "not valid data"));
                }catch (Exception e) {
                    //we have a problem
                    errors.add(new ErrorMessage("fatal_error", e.getMessage()));
                }
            }
        }
        if (errors.isEmpty()){
            response.sendRedirect(request.getContextPath() + "/app" + ApplicationContext.CINEMA_ADDRESS + "?cinema=" + cinemaId);
        }else{
            request.setAttribute("errors", errors);

            RequestDispatcher dispatcher = request.getRequestDispatcher(ApplicationContext.JSP_MOVIE_PAGE);
            dispatcher.forward(request, response);
        }
     }
}
