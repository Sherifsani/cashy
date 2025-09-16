package com.cashy.cashy.analytics.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.cashy.cashy.auth.model.UserProfile;
import com.cashy.cashy.auth.service.UserService;
import com.cashy.cashy.budget.service.BudgetService;
import com.cashy.cashy.category.service.CategoryService;
import com.cashy.cashy.transaction.exception.UserNotFoundException;
import com.cashy.cashy.transaction.service.TransactionService;
import com.cashy.cashy.transaction.model.Transaction;
import com.cashy.cashy.transaction.model.TransactionType;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AnalyticsService {
    private final UserService userService;
    private final TransactionService transactionService;
    private final CategoryService categoryService;
    private final BudgetService budgetService;

    public UserProfile validateAndGetUserProfile(UUID userId) {
        return userService.findUserById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    }

    // get all time total income or expense for a user
    public BigDecimal getTotalTransactions(UUID userId, TransactionType transactionType) {
        UserProfile user = validateAndGetUserProfile(userId);
        List<Transaction> transactions = user.getTransactions();
        return transactions.stream()
                .filter(t -> t.getTransactionType() == transactionType)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // get total income or expense for a user in a specific time period
    public BigDecimal getTotalTransactionInPeriod(
            UUID userId, LocalDate fromDate, LocalDate toDate, TransactionType transactionType) {
        if (fromDate == null || toDate == null) {
            throw new IllegalArgumentException("Date parameters cannot be null");
        }

        UserProfile user = validateAndGetUserProfile(userId);

        return user.getTransactions().stream()
                .filter(t -> t.getTransactionType() == transactionType)
                .filter(t -> t.getTransactionDate() != null &&
                        !t.getTransactionDate().isBefore(fromDate) &&
                        !t.getTransactionDate().isAfter(toDate))
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // get net cash flow
    public BigDecimal getNetCashFlow(UUID userId) {
        UserProfile user = validateAndGetUserProfile(userId);
        BigDecimal totalIncome = getTotalTransactions(userId, TransactionType.INCOME);
        BigDecimal totalExpense = getTotalTransactions(userId, TransactionType.EXPENSE);
        return totalIncome.subtract(totalExpense);
    }

    // get spending by category in a specific time period
    public Map<String, BigDecimal> getSpendingByCategory(
            UUID userId, LocalDate fromDate, LocalDate toDate, TransactionType transactionType) {

        UserProfile user = validateAndGetUserProfile(userId);

        return user.getTransactions().stream()
                .filter(t -> t.getTransactionType() == transactionType)
                .filter(t -> t.getTransactionDate() != null &&
                        !t.getTransactionDate().isBefore(fromDate) &&
                        !t.getTransactionDate().isAfter(toDate))
                .filter(t -> t.getCategory() != null) // Add null check
                .collect(Collectors.groupingBy(
                        t -> t.getCategory().getCategoryName(),
                        Collectors.reducing(BigDecimal.ZERO,
                                Transaction::getAmount,
                                BigDecimal::add)));
    }

}
