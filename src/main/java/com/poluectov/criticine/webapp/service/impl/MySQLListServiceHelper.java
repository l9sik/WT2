package com.poluectov.criticine.webapp.service.impl;

import com.poluectov.criticine.webapp.dao.connectionpool.ConnectionPool;
import com.poluectov.criticine.webapp.dao.connectionpool.WrappedConnection;
import com.poluectov.criticine.webapp.dao.mysqldao.MySQLCinemaTypeDao;
import com.poluectov.criticine.webapp.dao.mysqldao.MySQLCreateFunction;
import com.poluectov.criticine.webapp.exception.DataBaseNotAvailableException;
import com.poluectov.criticine.webapp.exception.StatementNotCreatedException;
import com.poluectov.criticine.webapp.model.data.Cinema;
import com.poluectov.criticine.webapp.model.data.CinemaType;
import com.poluectov.criticine.webapp.service.Filter;
import org.eclipse.tags.shaded.org.apache.xpath.functions.Function2Args;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.poluectov.criticine.webapp.dao.mysqldao.MySQLCinemaDao.*;
import static com.poluectov.criticine.webapp.dao.mysqldao.MySQLCinemaDao.CINEMA_FK_CINEMA_TYPE;

public class MySQLListServiceHelper {
    public static void addFilter(Filter filter, StringBuilder countSqlBuilder) {
        if (filter != null && filter.getFilters() != null && !filter.getFilters().isEmpty()) {
            countSqlBuilder.append(" WHERE ");
            for (Map.Entry<String, Object> entry : filter.getFilters().entrySet()) {
                countSqlBuilder.append(entry.getKey()).append("=? AND ");
            }
            // Remove the trailing "AND"
            countSqlBuilder.setLength(countSqlBuilder.length() - 5);
        }
    }

    public static int setFilterStatements(PreparedStatement statement, int parameterIndex, Filter filter) throws SQLException {
        if (filter != null && filter.getFilters() != null && !filter.getFilters().isEmpty()) {
            for (Map.Entry<String, Object> entry : filter.getFilters().entrySet()) {
                statement.setString(parameterIndex++, entry.getValue().toString());
            }
        }
        return parameterIndex;
    }

    public static <T> List<T> getList(ConnectionPool connectionPool, Filter filter, int page,
                                      SQLFilteredRequestBuilderFunction buildListSQL,
                                      SQLFilteredListStatementConsumer statementConsumer,
                                      MySQLCreateFunction<T> createFunction) throws SQLException{

        List<T> list = new ArrayList<>();

        try (WrappedConnection connection = connectionPool.getConnection()) {
            String sql = buildListSQL.apply(filter, page);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statementConsumer.accept(statement, filter, page);

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()){
                        T obj = createFunction.apply(resultSet);

                        list.add(obj);
                    }
                }
            }
        } catch (DataBaseNotAvailableException e) {
            throw e;
        }catch (StatementNotCreatedException e){
            e.printStackTrace();
            throw e;
        }catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return list;
    }

    public static int getListCount(ConnectionPool connectionPool, Filter filter,
                                   SQLFilteredRequestBuilderFunction builderFunction,
                                   SQLFilteredListStatementConsumer statementConsumer) throws SQLException{
        try (WrappedConnection connection = connectionPool.getConnection()) {
            String sql = builderFunction.apply(filter, -1);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statementConsumer.accept(statement, filter, -1);

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

}
