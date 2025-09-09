package com.cashy.cashy.auth.exception;

public class TransactionNotFound extends RuntimeException {
    public TransactionNotFound(Long transactionId) {
        super("transaction id " + transactionId + " not found");
    }
}
