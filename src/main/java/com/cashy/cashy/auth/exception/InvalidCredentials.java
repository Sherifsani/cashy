package com.cashy.cashy.auth.exception;

public class InvalidCredentials extends RuntimeException {
    public InvalidCredentials(String message) {
        super("Invalid credentials: " + message);
    }
}
