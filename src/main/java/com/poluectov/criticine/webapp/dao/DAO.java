package com.poluectov.criticine.webapp.dao;

import java.sql.SQLException;

public interface DAO<T> {

    T get(int id) throws SQLException;
    void create(T t) throws SQLException;
    void update(int id, T t) throws SQLException;
}
