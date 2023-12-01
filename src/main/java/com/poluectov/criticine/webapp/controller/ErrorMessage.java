package com.poluectov.criticine.webapp.controller;

public class ErrorMessage {
    private String error;
    private String message;
    public ErrorMessage(String error) {
        this.error = error;
        this.message = error;
    }
    /**
     * Returns a string representation of the ErrorMessage object.
     *
     * @return a string representation of the ErrorMessage object.
     */
    @Override
    public String toString() {
        return "ErrorMessage{" +
                "error='" + error + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public static final String DB_DATA_ERROR = "error.db.data";
    public static final String DB_CONNECTION = "error.db.connection";
    public static final String DB_NOT_WORKING = "error.db.not_working";
    public static final String DB_ERROR = "error.db.error";
    public static final String VALIDATION_ERROR = "error.validation";
    public static final String PICTURE_ERROR = "error.picture";
    public static final String YEAR_ERROR = "error.year";
    public static final String USER_NOT_FOUND = "error.user.not_found";
    public static final String USER_INVALID_DATA = "error.user.invalid_data";
    public static final String USER_NOT_AUTHORIZED = "error.user.authorization";



}
