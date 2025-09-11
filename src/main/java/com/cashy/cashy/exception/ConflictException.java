package com.cashy.cashy.exception;

public abstract class ConflictException extends BusinessException {
    public ConflictException(String message) {
        super(message);
    }
}
