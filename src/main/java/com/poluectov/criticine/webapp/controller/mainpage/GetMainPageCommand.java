package com.poluectov.criticine.webapp.controller.mainpage;

import com.poluectov.criticine.webapp.ApplicationContext;
import com.poluectov.criticine.webapp.controller.ErrorMessage;
import com.poluectov.criticine.webapp.controller.ServletCommand;
import com.poluectov.criticine.webapp.exception.DataBaseNotAvailableException;
import com.poluectov.criticine.webapp.model.data.Cinema;
import com.poluectov.criticine.webapp.service.CinemaListService;
import com.poluectov.criticine.webapp.service.Filter;
import com.poluectov.criticine.webapp.service.ServiceFilter;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GetMainPageCommand implements ServletCommand {
    @Override
    public void execute(ServletRequest request, ServletResponse response) throws ServletException, IOException {

        List<ErrorMessage> errors = new ArrayList<>();
        List<Cinema> cinemas = new ArrayList<>();

        int page = getPage(request.getParameter("page"));
        String sortBy = request.getParameter("sortBy");
        Filter filter = new ServiceFilter(null, sortBy);

        try {
            cinemas = ApplicationContext.INSTANCE.getCinemaListService().getCinemaList(filter, page);
        }catch (DataBaseNotAvailableException e){
            errors.add(new ErrorMessage("data_base_not_available",
                    "Data base not available right now. Please try later"));
        }catch (SQLException e){
            e.printStackTrace();
        }

        System.out.println(cinemas);

        request.setAttribute("cinemas", cinemas);
        request.setAttribute("errors", errors);

        RequestDispatcher dispatcher = request.getRequestDispatcher(ApplicationContext.JSP_MAIN_PAGE);
        dispatcher.forward(request, response);
    }

    int getPage(String page){
        if (page == null)
            return 0;
        int p;
        try{
            p = Integer.parseInt(page);
        }catch (NumberFormatException e) {
            p = 0;
        }
        return Math.max(p, 0);
    }

}
