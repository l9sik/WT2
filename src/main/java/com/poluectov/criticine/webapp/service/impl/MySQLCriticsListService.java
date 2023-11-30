package com.poluectov.criticine.webapp.service.impl;

import com.poluectov.criticine.webapp.dao.connectionpool.ConnectionPool;
import com.poluectov.criticine.webapp.dao.mysqldao.MySQLCinemaDao;
import com.poluectov.criticine.webapp.dao.mysqldao.MySQLUserCinemaReviewDao;
import com.poluectov.criticine.webapp.dao.mysqldao.MySQLUserDao;
import com.poluectov.criticine.webapp.model.data.Cinema;
import com.poluectov.criticine.webapp.model.data.User;
import com.poluectov.criticine.webapp.model.data.UserCinemaReview;
import com.poluectov.criticine.webapp.service.CriticsListService;
import com.poluectov.criticine.webapp.service.Filter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static com.poluectov.criticine.webapp.dao.mysqldao.MySQLCinemaDao.DB;

public class MySQLCriticsListService implements CriticsListService {
    private int pageLimit;
    ConnectionPool connectionPool;
    public MySQLCriticsListService(ConnectionPool connectionPool, int pageLimit) {
        this.connectionPool = connectionPool;
        this.pageLimit = pageLimit;
    }
    @Override
    public List<UserCinemaReview> getCriticsList(Filter filter, int page) throws SQLException {
        return MySQLListServiceHelper.getList(connectionPool, filter, page,
                this::buildUserCinemaReviewListSql, this::setCriticsListSqlStatement, this::createFunction);
    }
    @Override
    public List<UserCinemaReview> getCriticsList() throws SQLException {
        return getCriticsList(null, 0);
    }
    @Override
    public int getCriticsListCount(Filter filter) throws SQLException {
        return MySQLListServiceHelper.getListCount(connectionPool, filter,
                (f, p) -> buildCriticsListCountSql(f),
                (s, f, p) -> setCriticsListCountSqlStatement(s, f));
    }
    @Override
    public int getCriticsListCount() throws SQLException {
        return getCriticsListCount(null) ;
    }
    private String buildUserCinemaReviewListSql(Filter filter, int page) {
    /*
    SELECT
        user_cinema_review.*,
        cinema.*,
        user.*
    FROM
        user_cinema_review
        JOIN cinema ON user_cinema_review.fk_cinema_id = cinema.cinema_id
        JOIN user ON user_cinema_review.fk_user_id = user.user_id
     */
        StringBuilder sqlBuilder = new StringBuilder("SELECT ")
                .append(MySQLUserCinemaReviewDao.DB).append(".*, ")
                .append(MySQLCinemaDao.DB).append(".*, ")
                .append(MySQLUserDao.DB).append(".*")
                .append(" FROM ").append(MySQLUserCinemaReviewDao.DB)
                .append(" JOIN ").append(MySQLCinemaDao.DB).append(" ON ")
                .append(MySQLUserCinemaReviewDao.DB).append(".").append(MySQLUserCinemaReviewDao.FK_CINEMA_ID).append(" = ")
                .append(MySQLCinemaDao.DB).append(".").append(MySQLCinemaDao.CINEMA_ID)
                .append(" JOIN ").append(MySQLUserDao.DB).append(" ON ")
                .append(MySQLUserCinemaReviewDao.DB).append(".").append(MySQLUserCinemaReviewDao.FK_USER_ID).append(" = ")
                .append(MySQLUserDao.DB).append(".").append(MySQLUserDao.USER_ID);

        // Add WHERE clause for filters
        addFilters(filter, sqlBuilder);

        // Add ORDER BY clause for sorting
        if (filter != null && filter.getSortBy() != null && !filter.getSortBy().isEmpty()) {
            sqlBuilder.append(" ORDER BY ").append(filter.getSortBy());
        }

        sqlBuilder.append(" LIMIT ?");

        if (page > 0) {
            sqlBuilder.append(" OFFSET ?");
        }

        return sqlBuilder.toString();
    }
    public String buildCriticsListCountSql(Filter filter) {
        StringBuilder countSqlBuilder = new StringBuilder("SELECT COUNT(*) FROM ").append(MySQLUserCinemaReviewDao.DB);

        // Add WHERE clause for filters
        addFilters(filter, countSqlBuilder);

        return countSqlBuilder.toString();
    }
    private UserCinemaReview createFunction(ResultSet resultSet) throws SQLException {

        int userReviewId = resultSet.getInt(MySQLUserCinemaReviewDao.USER_REVIEW_ID);
        int fkUserId = resultSet.getInt(MySQLUserCinemaReviewDao.FK_USER_ID);
        int fkCinemaId = resultSet.getInt(MySQLUserCinemaReviewDao.FK_CINEMA_ID);
        int userRating = resultSet.getInt(MySQLUserCinemaReviewDao.USER_RATING);
        String userReview = resultSet.getString(MySQLUserCinemaReviewDao.USER_REVIEW);

        UserCinemaReview review = new UserCinemaReview(userReviewId, fkUserId, fkCinemaId, userRating, userReview);


        // Cinema columns
        int cinemaId = resultSet.getInt(MySQLCinemaDao.CINEMA_ID);
        String cinemaName = resultSet.getString(MySQLCinemaDao.CINEMA_NAME);
        float cinemaRating = resultSet.getFloat(MySQLCinemaDao.CINEMA_RATING);
        int cinemaCreationYear = resultSet.getInt(MySQLCinemaDao.CINEMA_CREATING_YEAR);
        String cinemaPictureFile = resultSet.getString(MySQLCinemaDao.CINEMA_PICTURE);
        int cinemaTypeId = resultSet.getInt(MySQLCinemaDao.CINEMA_FK_CINEMA_TYPE);


        // Add more cinema columns as needed
        Cinema cinema = new Cinema(cinemaId, cinemaName, cinemaRating,
                cinemaCreationYear, cinemaPictureFile, cinemaTypeId);
        // User columns
        int userId = resultSet.getInt(MySQLUserDao.USER_ID);
        String userName = resultSet.getString(MySQLUserDao.USER_NAME);
        String userEmail = resultSet.getString(MySQLUserDao.USER_EMAIL);
        String userPasswordHash = resultSet.getString(MySQLUserDao.USER_PASSWORD_HASH);
        boolean userIsVerified = resultSet.getBoolean(MySQLUserDao.USER_IS_VERIFIED);
        int userRole = resultSet.getInt(MySQLUserDao.USER_ROLE);
        int userStatus = resultSet.getInt(MySQLUserDao.USER_STATUS);

        User user = new User(userId, userName, userEmail, userPasswordHash, userIsVerified, userRole, userStatus);

        review.setCinema(cinema);
        review.setUser(user);

        return review;
    }
    private void setCriticsListSqlStatement(PreparedStatement statement, Filter filter, int page) throws SQLException {
        // Add WHERE clause for filters
        int parameterIndex = MySQLListServiceHelper.setFilterStatements(statement, 1, filter);

        //ORDER was already set

        //LIMIT
        statement.setInt(parameterIndex++, pageLimit);
        if (page > 0) {
            //OFFSET
            statement.setInt(parameterIndex++, pageLimit * page);
        }
    }
    private void setCriticsListCountSqlStatement(PreparedStatement statement, Filter filter) throws SQLException{
        MySQLListServiceHelper.setFilterStatements(statement, 1, filter);
    }
    private void addFilters(Filter filter, StringBuilder countSqlBuilder) {
        MySQLListServiceHelper.addFilter(filter, countSqlBuilder);
    }


}
