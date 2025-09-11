package com.cashy.cashy.auth.exception;

import com.cashy.cashy.exception.ResourceNotFoundException;

public class TransactionNotFoundException extends ResourceNotFoundException {
    public TransactionNotFoundException(Long transactionId) {
        super("Transaction", transactionId);
    }
}
