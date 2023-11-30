package com.poluectov.criticine.webapp.service;

import com.poluectov.criticine.webapp.model.data.UserCinemaReview;

import java.sql.SQLException;
import java.util.List;

public interface CriticsListService {

    List<UserCinemaReview> getCriticsList(Filter filter, int page) throws SQLException;
    List<UserCinemaReview> getCriticsList() throws SQLException;
    int getCriticsListCount(Filter filter) throws SQLException;
    int getCriticsListCount() throws SQLException;

}
