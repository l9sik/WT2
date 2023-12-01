package com.poluectov.criticine.webapp.dao.connectionpool;

import com.poluectov.criticine.webapp.ApplicationContext;
import com.poluectov.criticine.webapp.controller.criticspage.GetCriticsCommand;
import com.poluectov.criticine.webapp.exception.DataBaseNotAvailableException;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class BlockedQueueConnectionPool implements ConnectionPool {

    Logger logger = Logger.getLogger(BlockedQueueConnectionPool.class);
    private final String url;
    private final String userName;
    private final String password;
    private final BlockingQueue<Connection> queue;

    final int INITIAL_CAPACITY = 10;
    public BlockedQueueConnectionPool(String url, String userName, String password){
        //Inititalising db;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
        this.url = url;
        this.userName = userName;
        this.password = password;
        queue = new LinkedBlockingDeque<>(INITIAL_CAPACITY);
    }

    @Override
    public WrappedConnection getConnection() throws DataBaseNotAvailableException {
        Connection connection = queue.poll();

        if (connection == null){
            try {
                connection = createConnection();
            }catch (SQLException e){
                logger.error(e);
                throw new DataBaseNotAvailableException(e);
            }
        }
        return new WrappedSQLConnection(this, connection);
    }

    @Override
    public void releaseConnection(Connection connection) {
        boolean result = queue.offer(connection);
        if (!result){
            System.err.println("[WARNING] ConnectionPool out of capacity");
        }
    }

    private Connection createConnection() throws SQLException {
        return DriverManager.getConnection(url, userName, password);
    }
}
