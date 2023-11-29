package com.poluectov.criticine.webapp.controller;

public class ErrorMessage {
    private String error;
    private String message;
    public ErrorMessage(String error, String message) {
        this.error = error;
        this.message = message;
    }

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

    public static final String DB_CONNECTION = "db_connection";
    public static final String DB_ERROR = "db_error";
    public static final String DB_NOT_WORKING = "db_not_working";
    public static final String DB_DATA_ERROR = "db_data";

}
