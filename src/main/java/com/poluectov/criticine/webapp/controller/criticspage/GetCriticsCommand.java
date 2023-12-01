package com.poluectov.criticine.webapp.controller.criticspage;

import com.poluectov.criticine.webapp.ApplicationContext;
import com.poluectov.criticine.webapp.controller.ErrorMessage;
import com.poluectov.criticine.webapp.controller.ServletCommand;
import com.poluectov.criticine.webapp.exception.DataBaseNotAvailableException;
import com.poluectov.criticine.webapp.model.data.UserCinemaReview;
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

public class GetCriticsCommand implements ServletCommand {

    Logger logger = Logger.getLogger(GetCriticsCommand.class);
    @Override
    public void execute(ServletRequest req, ServletResponse resp) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        List<UserCinemaReview> reviews = new ArrayList<>();
        List<ErrorMessage> errors = new ArrayList<>();
        String pageStr = request.getParameter("page");
        int page = 0;
        if (pageStr != null) {
            try{
                page = Integer.parseInt(pageStr);
            }catch (NumberFormatException ignored){}
        }
        try{
             reviews = ApplicationContext.INSTANCE.getCriticsListService().getCriticsList(null, page);
        }catch (DataBaseNotAvailableException e){
            logger.error(e);
            errors.add(new ErrorMessage(ErrorMessage.DB_CONNECTION));
        }catch (SQLException e){
            e.printStackTrace();
            errors.add(new ErrorMessage(ErrorMessage.DB_ERROR));
        }

        request.setAttribute("reviews", reviews);
        request.setAttribute("errors", errors);

        RequestDispatcher dispatcher = request.getRequestDispatcher(ApplicationContext.JSP_CRITICS_PAGE);
        dispatcher.forward(request, response);
    }
}
