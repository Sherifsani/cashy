package com.cashy.cashy.budget.exceptions;

public class BudgetNotFoundException extends RuntimeException {

    public BudgetNotFoundException(Long budgetId) {

        super("Budget not found with ID: " + budgetId);
    }
}
