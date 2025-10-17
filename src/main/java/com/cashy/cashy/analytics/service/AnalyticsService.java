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

    public int getMonthlyTransactionCount(UUID userId, LocalDate fromDate, LocalDate toDate){
        UserProfile user = validateAndGetUserProfile(userId);
        return (int) user.getTransactions().stream()
                .filter(t -> t.getTransactionDate() != null &&
                        !t.getTransactionDate().isBefore(fromDate) &&
                        !t.getTransactionDate().isAfter(toDate))
                .count();
    }

    public Map<String, Object> getBudgetPerformance(UUID userId) {
        return Map.of("message", "Budget performance not implemented yet");
    }

    public Map<String, Object> getYearlySummary(UUID userId, int year) {
        return Map.of("message", "Yearly summary not implemented yet", "year", year);
    }

    public Map<String, Object> getTrends(UUID userId, int months) {
        return Map.of("message", "Trends not implemented yet", "months", months);
    }

    // get all-time spending by category
    public Map<String, BigDecimal> getAllTimeSpendingByCategory(UUID userId) {
        UserProfile user = validateAndGetUserProfile(userId);
        return user.getTransactions().stream()
                .filter(t -> t.getTransactionType() == TransactionType.EXPENSE)
                .filter(t -> t.getCategory() != null)
                .collect(Collectors.groupingBy(
                        t -> t.getCategory().getCategoryName(),
                        Collectors.reducing(BigDecimal.ZERO,
                                Transaction::getAmount,
                                BigDecimal::add)));
    }

    public Map<String, Object> getDashboardAnalytics(UUID userId) {
        UserProfile user = validateAndGetUserProfile(userId);
        
        // Monthly spending data for line chart (last 6 months)
        List<Map<String, Object>> monthlySpending = getMonthlySpendingData(user);
        
        // Category breakdown for pie chart
        List<Map<String, Object>> categoryBreakdown = getCategoryBreakdownData(user);
        
        // Income vs Expense for bar chart
        Map<String, Object> incomeVsExpense = getIncomeVsExpenseData(user);
        
        // Summary statistics
        Map<String, Object> summary = getSummaryStats(user);
        
        return Map.of(
            "monthlySpending", monthlySpending,
            "categoryBreakdown", categoryBreakdown,
            "incomeVsExpense", incomeVsExpense,
            "summary", summary
        );
    }

    private List<Map<String, Object>> getMonthlySpendingData(UserProfile user) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusMonths(6);
        
        List<Map<String, Object>> monthlyData = new ArrayList<>();
        
        for (int i = 0; i < 6; i++) {
            LocalDate monthStart = startDate.plusMonths(i).withDayOfMonth(1);
            LocalDate monthEnd = monthStart.plusMonths(1).minusDays(1);
            
            BigDecimal monthlyTotal = user.getTransactions().stream()
                .filter(t -> t.getTransactionDate() != null &&
                           !t.getTransactionDate().isBefore(monthStart) &&
                           !t.getTransactionDate().isAfter(monthEnd) &&
                           t.getTransactionType() == com.cashy.cashy.transaction.model.TransactionType.EXPENSE)
                .map(t -> t.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            monthlyData.add(Map.of(
                "month", monthStart.getMonth().name(),
                "year", monthStart.getYear(),
                "amount", monthlyTotal
            ));
        }
        
        return monthlyData;
    }

    private List<Map<String, Object>> getCategoryBreakdownData(UserProfile user) {
        Map<String, BigDecimal> categoryTotals = user.getTransactions().stream()
            .filter(t -> t.getTransactionType() == com.cashy.cashy.transaction.model.TransactionType.EXPENSE)
            .collect(Collectors.groupingBy(
                t -> t.getCategory() != null ? t.getCategory().getCategoryName() : "Uncategorized",
                Collectors.reducing(BigDecimal.ZERO, t -> t.getAmount(), BigDecimal::add)
            ));
        
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<String, BigDecimal> entry : categoryTotals.entrySet()) {
            Map<String, Object> categoryData = new HashMap<>();
            categoryData.put("category", entry.getKey());
            categoryData.put("amount", entry.getValue());
            categoryData.put("percentage", calculatePercentage(entry.getValue(), getTotalExpenses(user)));
            result.add(categoryData);
        }
        
        return result;
    }

    private Map<String, Object> getIncomeVsExpenseData(UserProfile user) {
        BigDecimal totalIncome = user.getTransactions().stream()
            .filter(t -> t.getTransactionType() == com.cashy.cashy.transaction.model.TransactionType.INCOME)
            .map(t -> t.getAmount())
            .reduce(BigDecimal.ZERO, BigDecimal::add);
            
        BigDecimal totalExpenses = getTotalExpenses(user);
        
        return Map.of(
            "income", totalIncome,
            "expenses", totalExpenses,
            "netAmount", totalIncome.subtract(totalExpenses)
        );
    }

    private Map<String, Object> getSummaryStats(UserProfile user) {
        BigDecimal totalIncome = user.getTransactions().stream()
            .filter(t -> t.getTransactionType() == com.cashy.cashy.transaction.model.TransactionType.INCOME)
            .map(t -> t.getAmount())
            .reduce(BigDecimal.ZERO, BigDecimal::add);
            
        BigDecimal totalExpenses = getTotalExpenses(user);
        int transactionCount = user.getTransactions().size();
        
        return Map.of(
            "totalIncome", totalIncome,
            "totalExpenses", totalExpenses,
            "netAmount", totalIncome.subtract(totalExpenses),
            "transactionCount", transactionCount,
            "averageTransaction", transactionCount > 0 ? 
                totalExpenses.add(totalIncome).divide(BigDecimal.valueOf(transactionCount), 2, BigDecimal.ROUND_HALF_UP) : 
                BigDecimal.ZERO
        );
    }

    private BigDecimal getTotalExpenses(UserProfile user) {
        return user.getTransactions().stream()
            .filter(t -> t.getTransactionType() == com.cashy.cashy.transaction.model.TransactionType.EXPENSE)
            .map(t -> t.getAmount())
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private double calculatePercentage(BigDecimal amount, BigDecimal total) {
        if (total.compareTo(BigDecimal.ZERO) == 0) return 0.0;
        return amount.divide(total, 4, BigDecimal.ROUND_HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .doubleValue();
    }

}
