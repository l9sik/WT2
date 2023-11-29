package com.poluectov.criticine.webapp.exception;

import java.sql.SQLException;

public class DataBaseNotAvailableException extends SQLException {

    public DataBaseNotAvailableException(Throwable cause) {
        super(cause);
    }

    public DataBaseNotAvailableException() {
        super();
    }

    public DataBaseNotAvailableException(String reason, Throwable cause) {
        super(reason, cause);
    }
}
