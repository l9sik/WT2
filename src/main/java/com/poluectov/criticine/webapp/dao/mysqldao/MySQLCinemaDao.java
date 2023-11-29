package com.poluectov.criticine.webapp.dao.mysqldao;

import com.poluectov.criticine.webapp.dao.CinemaDAO;
import com.poluectov.criticine.webapp.dao.connectionpool.ConnectionPool;
import com.poluectov.criticine.webapp.dao.connectionpool.WrappedConnection;
import com.poluectov.criticine.webapp.exception.DataBaseNotAvailableException;
import com.poluectov.criticine.webapp.exception.StatementNotCreatedException;
import com.poluectov.criticine.webapp.model.data.Cinema;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLCinemaDao implements CinemaDAO {
    public static final String DB = "cinema";
    public static final String CINEMA_ID = "cinema_id";
    public static final String CINEMA_NAME = "cinema_name";
    public static final String CINEMA_RATING = "cinema_rating";
    public static final String CINEMA_CREATING_YEAR = "cinema_creation_year";
    public static final String CINEMA_PICTURE = "cinema_picture";
    public static final String CINEMA_FK_CINEMA_TYPE = "fk_cinema_type";
    private static final String getCinemaSQL = "SELECT * FROM "+DB+" WHERE " + CINEMA_ID + " = ?";
    private static final String insertUserSQL = "INSERT INTO "+DB+" ("
            + CINEMA_NAME + ","
            + CINEMA_RATING + ","
            + CINEMA_CREATING_YEAR + ","
            + CINEMA_PICTURE + ","
            + CINEMA_FK_CINEMA_TYPE
            + ") VALUES (?, ?, ?, ?, ?)";
    private static final String updateUserIdSQL = "UPDATE "+DB+" " +
            "SET " + CINEMA_NAME + "=?, "
            + CINEMA_RATING + "=?, "
            + CINEMA_CREATING_YEAR + "=?, "
            + CINEMA_PICTURE + "=?, "
            + CINEMA_FK_CINEMA_TYPE + "=?" +
            "WHERE " + CINEMA_ID + "=?";

    ConnectionPool connectionPool;

    public MySQLCinemaDao(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public Cinema get(int id) throws SQLException {
        MySQLDao<Cinema> cinemaMySQLDao = new MySQLDao<>();
        return cinemaMySQLDao.get(connectionPool.getConnection(), id, getCinemaSQL, this::createCinema);
    }

    Cinema createCinema(ResultSet resultSet) throws SQLException {
        return new Cinema(
                resultSet.getInt(CINEMA_ID),
                resultSet.getString(CINEMA_NAME),
                resultSet.getFloat(CINEMA_RATING),
                resultSet.getInt(CINEMA_CREATING_YEAR),
                resultSet.getString(CINEMA_PICTURE),
                resultSet.getInt(CINEMA_FK_CINEMA_TYPE)
        );
    }

    @Override
    public void create(Cinema cinema) throws SQLException {
        MySQLDao<Cinema> cinemaMySQLDao = new MySQLDao<>();
        cinemaMySQLDao.create(connectionPool.getConnection(), cinema, insertUserSQL, this::fillStatement);
    }

    void fillStatement(Cinema cinema, PreparedStatement statement) throws SQLException {
        statement.setString(1,cinema.getName());
        statement.setFloat(2, cinema.getRating());
        statement.setInt(3, cinema.getCreationYear());
        statement.setString(4, cinema.getPictureFile());
        statement.setInt(5, cinema.getCinemaTypeId());
    }

    @Override
    public void update(int id, Cinema cinema) throws SQLException {
        MySQLDao<Cinema> cinemaMySQLDao = new MySQLDao<>();
        cinemaMySQLDao.update(connectionPool.getConnection(), id, 6, cinema, updateUserIdSQL, this::fillStatement);
    }
}
