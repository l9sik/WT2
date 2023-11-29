package com.poluectov.criticine.webapp.dao.connectionpool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public interface WrappedConnection extends AutoCloseable {
    Connection getConnection();

    Statement createStatement() throws SQLException;

    PreparedStatement prepareStatement(String sql) throws SQLException;

}
