package com.cashy.cashy.transaction.exception;

import java.util.UUID;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(UUID userId) {
        super(String.format("User id %s not found", userId));
    }
}
