package com.iproject.crowd.exception;

public class LoginFailureException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public LoginFailureException() { }

    public LoginFailureException(String message) {
        super(message);
    }

    public LoginFailureException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoginFailureException(Throwable cause) {
        super(cause);
    }

    public LoginFailureException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
