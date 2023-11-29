package com.poluectov.criticine.webapp.dao.connectionpool;

import com.poluectov.criticine.webapp.exception.DataBaseNotAvailableException;

import java.sql.Connection;

public interface ConnectionPool {

    WrappedConnection getConnection() throws DataBaseNotAvailableException;

    void releaseConnection(Connection connection);
}
