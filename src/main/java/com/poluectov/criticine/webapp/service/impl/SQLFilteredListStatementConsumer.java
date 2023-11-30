package com.poluectov.criticine.webapp.service.impl;

import com.poluectov.criticine.webapp.service.Filter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface SQLFilteredListStatementConsumer {

    void accept(PreparedStatement statement, Filter filter, int page) throws SQLException;
}
