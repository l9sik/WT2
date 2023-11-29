package com.poluectov.criticine.webapp.dao.mysqldao;

import com.poluectov.criticine.webapp.ApplicationContext;
import com.poluectov.criticine.webapp.dao.UserDAO;
import com.poluectov.criticine.webapp.dao.connectionpool.ConnectionPool;
import com.poluectov.criticine.webapp.dao.connectionpool.WrappedConnection;
import com.poluectov.criticine.webapp.exception.DataBaseNotAvailableException;
import com.poluectov.criticine.webapp.exception.StatementNotCreatedException;
import com.poluectov.criticine.webapp.model.data.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLUserDao implements UserDAO {
    private static final String USER_ID = "user_id";
    private static final String USER_NAME = "user_name";
    private static final String USER_EMAIL = "user_email";
    private static final String USER_PASSWORD_HASH = "user_password_hash";
    private static final String USER_IS_VERIFIED = "user_is_verified";
    private static final String USER_ROLE = "fk_user_role";
    private static final String USER_STATUS = "user_status";

    private static final String getUserSQL = "SELECT * FROM user WHERE user_id = ?";
    private static final String insertUserSQL = "INSERT INTO user ("
            +USER_NAME+", "
            +USER_EMAIL+", "
            +USER_PASSWORD_HASH+", "
            +USER_IS_VERIFIED+", "
            +USER_ROLE+", "
            +USER_STATUS+")" +
            "VALUES (?, ?, ?, ?, ?, ?)";
    private static final String updateUserIdSQL = "UPDATE user " +
            "SET "
            + USER_NAME + "=?, "
            + USER_EMAIL + "=?, "
            + USER_PASSWORD_HASH + "=?, "
            + USER_IS_VERIFIED + "=?, "
            + USER_ROLE + "=?, "
            + USER_STATUS + "=? " + "WHERE "
            + USER_ID + "=?";

    private static final String isUserInDBSQL = "SELECT * FROM user WHERE "
            +USER_NAME+"=? AND "
            +USER_PASSWORD_HASH+"=?";

    private static final int USER_ID_UPDATE_REQUEST_COUNT = 7;
    ConnectionPool connectionPool;

    public MySQLUserDao(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public User get(int id) throws StatementNotCreatedException, SQLException {
        MySQLDao<User> userMySQLDao = new MySQLDao<>();
        return userMySQLDao.get(connectionPool.getConnection(), id, getUserSQL, this::createUser);
    }

    User createUser(ResultSet resultSet) throws SQLException {
        return new User(resultSet.getInt(USER_ID),
                resultSet.getString(USER_NAME),
                resultSet.getString(USER_EMAIL),
                resultSet.getString(USER_PASSWORD_HASH),
                resultSet.getBoolean(USER_IS_VERIFIED),
                resultSet.getInt(USER_ROLE),
                resultSet.getInt(USER_STATUS));
    }

    @Override
    public void create(User user) throws StatementNotCreatedException, SQLException {
        MySQLDao<User> userMySQLDao = new MySQLDao<>();
        userMySQLDao.create(connectionPool.getConnection(), user, insertUserSQL, this::setStatement);
    }

    @Override
    public void update(int id, User user) throws StatementNotCreatedException, SQLException {
        MySQLDao<User> userMySQLDao = new MySQLDao<>();
        userMySQLDao.update(connectionPool.getConnection(), id, USER_ID_UPDATE_REQUEST_COUNT,
                user, updateUserIdSQL, this::setStatement);
    }

    private void setStatement(User user, PreparedStatement statement) throws SQLException {
        statement.setString(1, user.getName());
        statement.setString(2, user.getEmail());
        statement.setString(3, user.getPasswordHash());
        statement.setBoolean(4, user.isVerified());
        statement.setInt(5, user.getRoleId());
        statement.setInt(6, user.getStatus());
    }



    @Override
    public User userByPassword(String name, String password) throws SQLException{
        try(WrappedConnection connection = connectionPool.getConnection()) {
            try(PreparedStatement statement = connection.prepareStatement(isUserInDBSQL)){
                statement.setString(1, name);
                statement.setString(1, password);
                try(ResultSet resultSet = statement.executeQuery()) {

                    if (resultSet.next()) {
                        return createUser(resultSet);
                    }
                }
            }
        }catch (DataBaseNotAvailableException | StatementNotCreatedException e) {
            throw e;
        }catch (SQLException e){
            e.printStackTrace();
            throw new SQLException("Exception when getting from dataBase", e);
        }catch (Exception e){
            //in case when .close method brings exception
            throw new RuntimeException("close() method in WrappedConnection failed", e);
        }
        return null;
    }
}
