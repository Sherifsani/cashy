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
}
