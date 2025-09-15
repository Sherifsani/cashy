package com.cashy.cashy.transaction.exception;

import com.cashy.cashy.exception.ResourceNotFoundException;

import java.util.UUID;

public class UserNotFoundException extends ResourceNotFoundException {
    public UserNotFoundException(UUID userId) {
        super("User with id not found: ", userId);
    }
}
