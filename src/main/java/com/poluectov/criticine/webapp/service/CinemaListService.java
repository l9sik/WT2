package com.poluectov.criticine.webapp.service;

import com.poluectov.criticine.webapp.model.data.Cinema;
import com.poluectov.criticine.webapp.model.data.CinemaType;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface CinemaListService {


    List<Cinema> getCinemaList(Filter filter, int page) throws SQLException;
    List<Cinema> getCinemaList() throws SQLException;

    int getCinemaListCount(Filter filter) throws SQLException;
    int getCinemaListCount() throws SQLException;

}
