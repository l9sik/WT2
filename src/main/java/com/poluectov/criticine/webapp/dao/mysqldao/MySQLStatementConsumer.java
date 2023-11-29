package com.poluectov.criticine.webapp.dao.mysqldao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.function.Consumer;
@FunctionalInterface
public interface MySQLStatementConsumer<T> {
    void accept(T o, PreparedStatement statement) throws SQLException;
}
