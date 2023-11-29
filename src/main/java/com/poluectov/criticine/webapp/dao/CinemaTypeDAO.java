package com.poluectov.criticine.webapp.dao;

import com.poluectov.criticine.webapp.model.data.CinemaType;

import java.sql.SQLException;
import java.util.List;

public interface CinemaTypeDAO {
    List<CinemaType> getAllTypes() throws SQLException;

    int getCinemaTypeId(String typeName) throws SQLException;
}