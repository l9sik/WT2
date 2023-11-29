package com.poluectov.criticine.webapp.dao.mysqldao;

import com.poluectov.criticine.webapp.exception.DataBaseNotAvailableException;
import com.poluectov.criticine.webapp.exception.StatementNotCreatedException;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface MySQLCreateFunction<T> {

    T apply(ResultSet resultSet) throws SQLException;

}
