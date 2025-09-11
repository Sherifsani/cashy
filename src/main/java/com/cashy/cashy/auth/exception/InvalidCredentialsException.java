package com.cashy.cashy.auth.exception;

import com.cashy.cashy.exception.BadRequestException;

public class InvalidCredentialsException extends BadRequestException {
    public InvalidCredentialsException(String message) {
        super("Invalid credentials: " + message);
    }
}
