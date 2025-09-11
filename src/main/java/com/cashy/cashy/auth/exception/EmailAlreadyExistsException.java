package com.cashy.cashy.auth.exception;

import com.cashy.cashy.exception.ConflictException;

public class EmailAlreadyExistsException extends ConflictException {

    public EmailAlreadyExistsException(String email) {
        super("Email already exists: " + email);
    }
}
