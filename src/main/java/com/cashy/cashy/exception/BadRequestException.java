package com.cashy.cashy.exception;

public abstract class BadRequestException extends BusinessException {
    public BadRequestException(String message) {
        super(message);
    }
}
