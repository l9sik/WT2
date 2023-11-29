package com.poluectov.criticine.webapp.service.impl;

import com.poluectov.criticine.webapp.dao.CinemaDAO;
import com.poluectov.criticine.webapp.dao.connectionpool.ConnectionPool;
import com.poluectov.criticine.webapp.dao.connectionpool.WrappedConnection;
import com.poluectov.criticine.webapp.dao.mysqldao.MySQLCinemaDao;
import com.poluectov.criticine.webapp.dao.mysqldao.MySQLCinemaTypeDao;
import com.poluectov.criticine.webapp.exception.DataBaseNotAvailableException;
import com.poluectov.criticine.webapp.exception.StatementNotCreatedException;
import com.poluectov.criticine.webapp.model.data.Cinema;
import com.poluectov.criticine.webapp.model.data.CinemaType;
import com.poluectov.criticine.webapp.service.CinemaListService;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.poluectov.criticine.webapp.dao.mysqldao.MySQLCinemaDao.*;

public class MySqlCinemaListService implements CinemaListService {
    ConnectionPool connectionPool;
    int pageLimit;
    public MySqlCinemaListService(ConnectionPool connectionPool, int pageLimit) {
        this.connectionPool = connectionPool;
        this.pageLimit = pageLimit;
    }

    @Override
    public List<Cinema> getCinemaList(Filter filter, int page) throws SQLException {

        List<Cinema> cinemaList = new ArrayList<>();

        try (WrappedConnection connection = connectionPool.getConnection()) {
            String sql = buildCinemaListSql(filter,page);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                setCinemaListSqlStatement(statement, filter, page);

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()){
                        Cinema cinema = new Cinema(
                                resultSet.getInt(CINEMA_ID),
                                resultSet.getString(CINEMA_NAME),
                                resultSet.getFloat(CINEMA_RATING),
                                resultSet.getInt(CINEMA_CREATING_YEAR),
                                resultSet.getString(CINEMA_PICTURE),
                                resultSet.getInt(CINEMA_FK_CINEMA_TYPE)
                        );
                        String cinemaType = resultSet.getString(MySQLCinemaTypeDao.CINEMA_TYPE_NAME);

                        cinema.setCinemaType(new CinemaType(cinema.getCinemaTypeId(), cinemaType));

                        cinemaList.add(cinema);
                    }
                }
            }
        } catch (DataBaseNotAvailableException e) {
            throw e;
        }catch (StatementNotCreatedException e){
            e.printStackTrace();
            throw e;
        }catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return cinemaList;
    }

    @Override
    public List<Cinema> getCinemaList() throws SQLException {
        return getCinemaList(new Filter(null, null), 0);
    }

    @Override
    public int getCinemaListCount(Filter filter) throws SQLException {
        try (WrappedConnection connection = connectionPool.getConnection()) {
            String sql = buildCinemaListCountSql(filter);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                setCinemaListCountStatement(statement, filter);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()){
                        return resultSet.getInt(1);
                    }
                }
            }
        } catch (DataBaseNotAvailableException e) {
            throw e;
        }catch (StatementNotCreatedException e){
            e.printStackTrace();
            throw e;
        }catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return 0;
    }

    @Override
    public int getCinemaListCount() throws SQLException {
        return getCinemaListCount(new Filter(null, null));
    }

    public String buildCinemaListSql(Filter filter, int page) {
        /*
        SELECT
            cinema.*,
            cinema_type.cinema_type_name
        FROM
            cinema
         */
        StringBuilder sqlBuilder = new StringBuilder("SELECT ")
                .append(DB).append(".*, ")
                .append(MySQLCinemaTypeDao.DB).append(".").append(MySQLCinemaTypeDao.CINEMA_TYPE_NAME)
                .append(" FROM ").append(DB);


        /*
        JOIN
            cinema_type ON cinema.fk_cinema_type = cinema_type.cinema_type_id
         */
        sqlBuilder.append(" JOIN ")
                .append(MySQLCinemaTypeDao.DB).append(" ON ")
                    .append(DB).append(".").append(CINEMA_FK_CINEMA_TYPE).append(" = ")
                .append(MySQLCinemaTypeDao.DB).append(".").append(MySQLCinemaTypeDao.CINEMA_TYPE_ID);

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

    public String buildCinemaListCountSql(Filter filter) {
        StringBuilder countSqlBuilder = new StringBuilder("SELECT COUNT(*) FROM ").append(DB);

        // Add WHERE clause for filters
        addFilters(filter, countSqlBuilder);

        return countSqlBuilder.toString();
    }

    private void setCinemaListCountStatement(PreparedStatement statement, Filter filter) throws SQLException {
        setFilterStatements(statement, 1,filter);
    }

    private int setFilterStatements(PreparedStatement statement, int parameterIndex, Filter filter) throws SQLException {
        if (filter != null && filter.getFilters() != null && !filter.getFilters().isEmpty()) {
            for (Map.Entry<String, Object> entry : filter.getFilters().entrySet()) {
                statement.setString(parameterIndex++, entry.getValue().toString());
            }
        }
        return parameterIndex;
    }

    private void addFilters(Filter filter, StringBuilder countSqlBuilder) {
        if (filter != null && filter.getFilters() != null && !filter.getFilters().isEmpty()) {
            countSqlBuilder.append(" WHERE ");
            for (Map.Entry<String, Object> entry : filter.getFilters().entrySet()) {
                countSqlBuilder.append(entry.getKey()).append("=? AND ");
            }
            // Remove the trailing "AND"
            countSqlBuilder.setLength(countSqlBuilder.length() - 5);
        }
    }

    void setCinemaListSqlStatement(PreparedStatement statement, Filter filter, int page) throws SQLException {
        // Add WHERE clause for filters
        int parameterIndex = setFilterStatements(statement, 1, filter);

        //ORDER was already set

        //LIMIT
        statement.setInt(parameterIndex++, pageLimit);
        if (page > 0) {
            //OFFSET
            statement.setInt(parameterIndex++, pageLimit * page);
        }
    }

}
