package com.cashy.cashy.budget.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptions extends RuntimeException {

    @ExceptionHandler(BudgetNotFoundException.class)
    public ResponseEntity<Map<String, String>> handle(BudgetNotFoundException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("budget", ex.getMessage());
        return ResponseEntity.badRequest().body(errors);
    }
}
