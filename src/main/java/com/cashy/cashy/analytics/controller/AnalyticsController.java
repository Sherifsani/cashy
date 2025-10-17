package com.cashy.cashy.analytics.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cashy.cashy.analytics.dto.ApiResponse;
import com.cashy.cashy.analytics.service.AnalyticsService;
import com.cashy.cashy.transaction.model.TransactionType;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users/{userId}/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/total")
    public ResponseEntity<ApiResponse<BigDecimal>> getTotalTransactions(
            @PathVariable UUID userId,
            @RequestParam TransactionType transactionType) {
        BigDecimal total = analyticsService.getTotalTransactions(userId, transactionType);
        return ResponseEntity.ok(ApiResponse.success("Total " + transactionType.name().toLowerCase() + " retrieved successfully", total));
    }

    @GetMapping("/period")
    public ResponseEntity<ApiResponse<BigDecimal>> getTotalInPeriod(
            @PathVariable UUID userId,
            @RequestParam LocalDate fromDate,
            @RequestParam LocalDate toDate,
            @RequestParam TransactionType transactionType) {
        BigDecimal total = analyticsService.getTotalTransactionInPeriod(userId, fromDate, toDate, transactionType);
        return ResponseEntity.ok(ApiResponse.success("Period " + transactionType.name().toLowerCase() + " retrieved successfully", total));
    }

    @GetMapping("/net-cash-flow")
    public ResponseEntity<ApiResponse<BigDecimal>> getNetCashFlow(@PathVariable UUID userId) {
        BigDecimal netFlow = analyticsService.getNetCashFlow(userId);
        return ResponseEntity.ok(ApiResponse.success("Net cash flow retrieved successfully", netFlow));
    }

    @GetMapping("/spending-by-category")
    public ResponseEntity<ApiResponse<Map<String, BigDecimal>>> getSpendingByCategory(
            @PathVariable UUID userId,
            @RequestParam LocalDate fromDate,
            @RequestParam LocalDate toDate,
            @RequestParam TransactionType transactionType) {
        Map<String, BigDecimal> categoryData = analyticsService.getSpendingByCategory(userId, fromDate, toDate, transactionType);
        return ResponseEntity.ok(ApiResponse.success("Category breakdown retrieved successfully", categoryData));
    }

    @GetMapping("/monthly-transactions")
    public ResponseEntity<ApiResponse<Map<String, String>>> getMonthlyTransactions(
            @PathVariable UUID userId,
            @RequestParam int year,
            @RequestParam int month
    ){
        LocalDate fromDate = LocalDate.of(year, month, 1);
        LocalDate toDate = fromDate.withDayOfMonth(fromDate.lengthOfMonth());

        BigDecimal monthlyExpense = analyticsService.getTotalTransactionInPeriod(userId, fromDate, toDate, TransactionType.EXPENSE);
        BigDecimal monthlyIncome = analyticsService.getTotalTransactionInPeriod(userId, fromDate, toDate, TransactionType.INCOME);
        int monthlyTransactionCount = analyticsService.getMonthlyTransactionCount(userId, fromDate, toDate);
        BigDecimal netMonthlyCashFlow = monthlyIncome.subtract(monthlyExpense);
        Map<String, String> monthlyData = Map.of(
                "monthlyExpense", monthlyExpense.toString(),
                "monthlyIncome", monthlyIncome.toString(),
                "monthlyTransactionCount", String.valueOf(monthlyTransactionCount),
                "netMonthlyCashFlow", netMonthlyCashFlow.toString()
        );
        return ResponseEntity.ok(ApiResponse.success("Monthly transactions data retrieved successfully", monthlyData));
    }

    @GetMapping("/yearly-summary")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getYearlySummary(
            @PathVariable UUID userId,
            @RequestParam int year) {
        Map<String, Object> summary = analyticsService.getYearlySummary(userId, year);
        return ResponseEntity.ok(ApiResponse.success("Yearly summary retrieved successfully", summary));
    }

    @GetMapping("/trends")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getTrends(
            @PathVariable UUID userId,
            @RequestParam int months) {
        Map<String, Object> trends = analyticsService.getTrends(userId, months);
        return ResponseEntity.ok(ApiResponse.success("Trends retrieved successfully", trends));
    }

    @GetMapping("/budget-performance")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getBudgetPerformance(@PathVariable UUID userId) {
        Map<String, Object> performance = analyticsService.getBudgetPerformance(userId);
        return ResponseEntity.ok(ApiResponse.success("Budget performance retrieved successfully", performance));
    }

    @GetMapping("/spending-by-category-all")
    public ResponseEntity<ApiResponse<Map<String, BigDecimal>>> getAllTimeSpendingByCategory(@PathVariable UUID userId) {
        Map<String, BigDecimal> categoryData = analyticsService.getAllTimeSpendingByCategory(userId);
        return ResponseEntity.ok(ApiResponse.success("All-time spending by category retrieved successfully", categoryData));
    }

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboardAnalytics(@PathVariable UUID userId) {
        Map<String, Object> analytics = analyticsService.getDashboardAnalytics(userId);
        return ResponseEntity.ok(ApiResponse.success("Dashboard analytics retrieved successfully", analytics));
    }
}
