package com.poluectov.criticine.webapp.dao.connectionpool;

import com.poluectov.criticine.webapp.exception.StatementNotCreatedException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class WrappedSQLConnection implements WrappedConnection{
    ConnectionPool pool;
    Connection connection;

    public WrappedSQLConnection(ConnectionPool pool, Connection connection){
        this.pool = pool;
        this.connection = connection;
    }
    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public Statement createStatement() throws StatementNotCreatedException {
        Statement s;
        try {
            s = connection.createStatement();
        }catch (SQLException e){
            throw new StatementNotCreatedException("Statement could not be created", e);
        }
        return s;
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        PreparedStatement statement;
        try{
            statement = connection.prepareStatement(sql);
        }catch (SQLException e){
            throw new StatementNotCreatedException("Prepared statement could not be created", e);
        }
        return statement;
    }

    @Override
    public void close(){
        pool.releaseConnection(connection);
    }
}
