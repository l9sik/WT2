package com.poluectov.criticine.webapp.service.impl;

import com.poluectov.criticine.webapp.dao.connectionpool.ConnectionPool;
import com.poluectov.criticine.webapp.dao.connectionpool.WrappedConnection;
import com.poluectov.criticine.webapp.dao.mysqldao.MySQLCinemaTypeDao;
import com.poluectov.criticine.webapp.exception.DataBaseNotAvailableException;
import com.poluectov.criticine.webapp.exception.StatementNotCreatedException;
import com.poluectov.criticine.webapp.model.data.Cinema;
import com.poluectov.criticine.webapp.model.data.CinemaType;
import com.poluectov.criticine.webapp.service.CinemaListService;
import com.poluectov.criticine.webapp.service.Filter;
import com.poluectov.criticine.webapp.service.ServiceFilter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.poluectov.criticine.webapp.dao.mysqldao.MySQLCinemaDao.*;

public class MySQLCinemaListService implements CinemaListService {
    ConnectionPool connectionPool;
    int pageLimit;
    public MySQLCinemaListService(ConnectionPool connectionPool, int pageLimit) {
        this.connectionPool = connectionPool;
        this.pageLimit = pageLimit;
    }

    @Override
    public List<Cinema> getCinemaList(Filter filter, int page) throws SQLException {
        return MySQLListServiceHelper.getList(connectionPool, filter, page,
                this::buildCinemaListSql, this::setCinemaListSqlStatement, this::cinemaCreateFunction);
    }

    private Cinema cinemaCreateFunction(ResultSet resultSet) throws SQLException {
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

        return cinema;
    }

    @Override
    public List<Cinema> getCinemaList() throws SQLException {
        return getCinemaList(null, 0);
    }

    @Override
    public int getCinemaListCount(Filter filter) throws SQLException {
        return MySQLListServiceHelper.getListCount(connectionPool, filter,
                (f, p) -> buildCinemaListCountSql(f), (s, f, p) -> setCinemaListCountStatement(s,f));
    }

    @Override
    public int getCinemaListCount() throws SQLException {
        return getCinemaListCount(new ServiceFilter(null, null));
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
        setFilterStatements(statement, filter);
    }

    private int setFilterStatements(PreparedStatement statement, Filter filter) throws SQLException {
        return MySQLListServiceHelper.setFilterStatements(statement, 1, filter);
    }

    private void addFilters(Filter filter, StringBuilder countSqlBuilder) {
        MySQLListServiceHelper.addFilter(filter, countSqlBuilder);
    }

    void setCinemaListSqlStatement(PreparedStatement statement, Filter filter, int page) throws SQLException {
        // Add WHERE clause for filters
        int parameterIndex = setFilterStatements(statement, filter);

        //ORDER was already set

        //LIMIT
        statement.setInt(parameterIndex++, pageLimit);
        if (page > 0) {
            //OFFSET
            statement.setInt(parameterIndex++, pageLimit * page);
        }
    }

}
