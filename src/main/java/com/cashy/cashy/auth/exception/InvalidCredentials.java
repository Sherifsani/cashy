package com.cashy.cashy.auth.exception;

import com.cashy.cashy.exception.BadRequestException;

public class InvalidCredentials extends BadRequestException {
    public InvalidCredentials(String message) {
        super("Invalid credentials: " + message);
    }
}
