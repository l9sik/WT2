package com.poluectov.criticine.webapp.dao.mysqldao;

import com.poluectov.criticine.webapp.dao.CinemaTypeDAO;
import com.poluectov.criticine.webapp.dao.connectionpool.ConnectionPool;
import com.poluectov.criticine.webapp.dao.connectionpool.WrappedConnection;
import com.poluectov.criticine.webapp.exception.DataBaseNotAvailableException;
import com.poluectov.criticine.webapp.exception.StatementNotCreatedException;
import com.poluectov.criticine.webapp.model.data.CinemaType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MySQLCinemaTypeDao implements CinemaTypeDAO {

    public static final String DB = "cinema_type";
    public static final String CINEMA_TYPE_ID = "cinema_type_id";
    public static final String CINEMA_TYPE_NAME = "cinema_type_name";
    private static final String getAllTypesSQL = "SELECT * FROM " + DB;
    List<CinemaType> bufferedList;

    ConnectionPool pool;

    public MySQLCinemaTypeDao(ConnectionPool pool) {
        this.pool = pool;
    }

    @Override
    public List<CinemaType> getAllTypes() throws SQLException {
        if (bufferedList == null || bufferedList.isEmpty()){
            bufferedList = getFromDataBase();
        }
        return bufferedList;
    }

    private List<CinemaType> getFromDataBase() throws SQLException{
        List<CinemaType> cinemaTypes = new ArrayList<>();
        try(WrappedConnection connection = pool.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(getAllTypesSQL)) {
                try (ResultSet resultSet = statement.executeQuery()) {

                    while (resultSet.next()){
                        cinemaTypes.add(new CinemaType(
                                resultSet.getInt(CINEMA_TYPE_ID),
                                resultSet.getString(CINEMA_TYPE_NAME)
                        ));
                    }
                }
            }
        }catch (StatementNotCreatedException | DataBaseNotAvailableException e){
            throw e;
        }catch (SQLException e){
            e.printStackTrace();
            throw new SQLException("Exception when getting a list of cinema types from data base ", e);
        }catch (Exception e){
            throw new RuntimeException("close() method in WrappedConnection failed", e);
        }
        return cinemaTypes;
    }

    @Override
    public int getCinemaTypeId(String typeName) throws SQLException {
        if (bufferedList == null || bufferedList.isEmpty()){
            bufferedList = getFromDataBase();
        }
        for (CinemaType cinemaType : bufferedList){
            if (cinemaType.getTypeName().equals(typeName)){
                return cinemaType.getId();
            }
        }
        return 1;
    }
}
