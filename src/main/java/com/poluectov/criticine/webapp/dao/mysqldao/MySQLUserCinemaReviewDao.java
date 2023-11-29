package com.poluectov.criticine.webapp.dao.mysqldao;

import com.poluectov.criticine.webapp.dao.UserCinemaReviewDAO;
import com.poluectov.criticine.webapp.dao.connectionpool.ConnectionPool;
import com.poluectov.criticine.webapp.model.data.UserCinemaReview;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLUserCinemaReviewDao implements UserCinemaReviewDAO {
    public static final String USER_REVIEW_ID = "user_review_id";
    public static final String FK_USER_ID = "fk_user_id";
    public static final String FK_CINEMA_ID = "fk_cinema_id";
    public static final String USER_RATING = "user_rating";
    public static final String USER_REVIEW = "user_review";
    public static final String DB = "user_cinema_review";


    private static final String getUserCinemaReviewSQL = "SELECT * FROM "+ DB +" WHERE " + USER_REVIEW_ID + " = ?";
    private static final String insertUserCinemaReviewSQL = "INSERT INTO " + DB + " ("
            + FK_USER_ID + ","
            + FK_CINEMA_ID + ","
            + USER_RATING + ","
            + USER_REVIEW
            + ") VALUES (?, ?, ?, ?)";

    private static final String updateUserCinemaReviewIdSQL = "UPDATE " + DB + " " +
            "SET " + FK_USER_ID + "=?, "
            + FK_CINEMA_ID + "=?, "
            + USER_RATING + "=?, "
            + USER_REVIEW + "=?" +
            "WHERE " + USER_REVIEW_ID + "=?";
    private static final int USER_REVIEW_ID_UPDATE_COUNT = 5;
    ConnectionPool connectionPool;

    public MySQLUserCinemaReviewDao(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public UserCinemaReview get(int id) throws SQLException {
        MySQLDao<UserCinemaReview> userCinemaReviewMySQLDao = new MySQLDao<>();


        return userCinemaReviewMySQLDao.get(connectionPool.getConnection(),
                id, getUserCinemaReviewSQL, this::createReview);
    }

    UserCinemaReview createReview(ResultSet resultSet) throws SQLException {
        return new UserCinemaReview(
                resultSet.getInt(USER_REVIEW_ID),
                resultSet.getInt(FK_USER_ID),
                resultSet.getInt(FK_CINEMA_ID),
                resultSet.getInt(USER_RATING),
                resultSet.getString(USER_REVIEW)
        );
    }

    void fillStatement(UserCinemaReview userCinemaReview, PreparedStatement statement) throws SQLException {
        statement.setInt(1, userCinemaReview.getUserId());
        statement.setInt(2, userCinemaReview.getCinemaId());
        statement.setInt(3, userCinemaReview.getRating());
        statement.setString(4, userCinemaReview.getReview());
    }

    @Override
    public void create(UserCinemaReview userCinemaReview) throws SQLException {
        MySQLDao<UserCinemaReview> mySQLDao = new MySQLDao<>();
        mySQLDao.create(connectionPool.getConnection(),
                userCinemaReview, insertUserCinemaReviewSQL, this::fillStatement);
    }

    @Override
    public void update(int id, UserCinemaReview userCinemaReview) throws SQLException {
        MySQLDao<UserCinemaReview> mySQLDao = new MySQLDao<>();
        mySQLDao.update(connectionPool.getConnection(),
                id, USER_REVIEW_ID_UPDATE_COUNT, userCinemaReview,
                updateUserCinemaReviewIdSQL, this::fillStatement);
    }
}
