package com.poluectov.criticine.webapp.dao.mysqldao;

import com.poluectov.criticine.webapp.controller.criticspage.GetCriticsCommand;
import com.poluectov.criticine.webapp.dao.DAO;
import com.poluectov.criticine.webapp.dao.connectionpool.WrappedConnection;
import com.poluectov.criticine.webapp.exception.DataBaseNotAvailableException;
import com.poluectov.criticine.webapp.exception.StatementNotCreatedException;
import com.poluectov.criticine.webapp.model.data.User;
import org.apache.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

public class MySQLDao<T> {

    Logger logger = Logger.getLogger(MySQLDao.class);


    /**
     * Gets one instance of <T> from data base
     *
     *
     * @param conn current connection to data base
     * @param id id of the instance <T> ypu are looking for
     * @param statementString a SQL request string where first parameter is id
     * @param createFunction a function that creates <T> from resultSet that
     *                       comes from data base after successfully responded
     * @return instane <T> or null if not found
     */
    T get(WrappedConnection conn,
          int id,
          String statementString,
          MySQLCreateFunction<T> createFunction) throws DataBaseNotAvailableException, StatementNotCreatedException, SQLException {
        try(WrappedConnection connection = conn) {
            try(PreparedStatement statement = connection.prepareStatement(statementString)){
                statement.setInt(1, id);
                try(ResultSet resultSet = statement.executeQuery()) {

                    if (resultSet.next()) {
                        return createFunction.apply(resultSet);
                    }
                }
            }
        }catch (DataBaseNotAvailableException | StatementNotCreatedException e) {
            logger.error(e);
            throw e;
        }catch (SQLException e){
            logger.error(e);
            throw new SQLException("Exception when getting from dataBase", e);
        }catch (Exception e){
            //in case when .close method brings exception
            logger.error("maybe .close() method in WrappedConnection failed", e);
            throw new RuntimeException("close() method in WrappedConnection failed", e);
        }
        logger.info(id + " in " + statementString + " not found");
        return null;
    }

    /**
     * Creates instance of <T> in data base
     * @param conn current connection to data base
     * @param o instance you want to create in data base
     * @param statementString SQL request string
     * @param statementConsumer consumer that sets parameters from @o to statementString
     * @throws SQLException data base brings errors
     */
    void create(WrappedConnection conn,
                T o, String statementString,
                MySQLStatementConsumer<T> statementConsumer) throws SQLException {

        try(WrappedConnection connection = conn) {
            try(PreparedStatement statement = connection.prepareStatement(statementString)){
                statementConsumer.accept(o, statement);
                statement.executeUpdate();
            }
        }catch (StatementNotCreatedException e) {
            logger.error(e);
            throw e;
        }catch (SQLException e) {
            logger.error(e);
            throw new SQLException("Exception while creating instance in Data Base", e);
        } catch (Exception e) {
            logger.error(e);
            throw new RuntimeException("close() method in WrappedConnection failed", e);
        }
    }

    /**
     * updates instance of <T> where id = @id
     * @param connection connection to data base
     * @param id id of instance in data base you want to update
     * @param idParameterNumber count of id parameter in SQL request string from @statementString
     *                          For example:
     *                          Update table SET t1=?, t2=? WHERE t_id=?
     *                          In that example @idParameterNumber equals 3.
     * @param o new instance
     * @param statmentString SQL request string
     * @param statementConsumer consumer that fills prepared statement with data from @o
     * @throws SQLException if data base brings error
     */
    void update(WrappedConnection connection,
                int id, int idParameterNumber, T o, String statmentString,
                MySQLStatementConsumer<T> statementConsumer) throws SQLException {
        try(connection){

            try(PreparedStatement statement = connection.prepareStatement(statmentString)){
                statementConsumer.accept(o, statement);
                statement.setInt(idParameterNumber, id);
                statement.executeUpdate();
            }

        }catch (StatementNotCreatedException e) {
            logger.error(e);
            throw e;
        }catch (SQLException e){
            logger.error(e);
            throw new SQLException("Exception while updating instance in Data Base", e);
        }catch (Exception e){
            logger.error(e);
            throw new RuntimeException("close() method in WrappedConnection failed", e);
        }
    }

}
