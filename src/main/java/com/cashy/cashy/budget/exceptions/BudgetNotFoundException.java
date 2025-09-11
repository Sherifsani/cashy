package com.cashy.cashy.budget.exceptions;

import com.cashy.cashy.exception.ResourceNotFoundException;

public class BudgetNotFoundException extends ResourceNotFoundException {

    public BudgetNotFoundException(Long budgetId) {
        super("Budget", budgetId);
    }
}
