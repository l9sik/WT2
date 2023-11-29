package com.poluectov.criticine.webapp.dao;

import com.poluectov.criticine.webapp.model.data.User;

import java.sql.SQLException;

public interface UserDAO extends DAO<User> {


    User userByPassword(String name, String password) throws SQLException;

}
