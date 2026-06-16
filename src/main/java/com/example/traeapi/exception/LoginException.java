package com.example.traeapi.exception;

public class LoginException extends RuntimeException {

    private final String errorCode;

    public LoginException(String message) {
        super(message);
        this.errorCode = "LOGIN_ERROR";
    }

    public String getErrorCode() {
        return errorCode;
    }
}
