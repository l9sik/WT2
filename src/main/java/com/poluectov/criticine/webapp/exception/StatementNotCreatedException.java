package com.poluectov.criticine.webapp.exception;

import java.sql.SQLException;

public class StatementNotCreatedException extends SQLException {

    public StatementNotCreatedException(String s, Throwable throwable){
        super(s, throwable);
    }
}
