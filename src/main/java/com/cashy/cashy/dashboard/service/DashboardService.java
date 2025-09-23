package com.cashy.cashy.dashboard.service;

import com.cashy.cashy.analytics.service.AnalyticsService;
import com.cashy.cashy.auth.model.UserProfile;
import com.cashy.cashy.auth.service.UserService;
import com.cashy.cashy.dashboard.dto.DashboardDTO;
import com.cashy.cashy.transaction.dto.TransactionResponseDTO;
import com.cashy.cashy.transaction.exception.UserNotFoundException;
import com.cashy.cashy.transaction.mapper.TransactionMapper;
import com.cashy.cashy.transaction.model.Transaction;
import com.cashy.cashy.transaction.model.TransactionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final UserService userService;
    private final AnalyticsService analyticsService;

    public DashboardDTO getDashboardData(UUID userId) {
        UserProfile user = userService.findUserById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        String username = user.getUsername();
        BigDecimal totalIncome = analyticsService.getTotalTransactions(userId, TransactionType.INCOME);
        BigDecimal totalExpense = analyticsService.getTotalTransactions(userId, TransactionType.EXPENSE);
        List<Transaction> recentTransactions = user.getTransactions().subList(Math.max(0, user.getTransactions().size() - 5), user.getTransactions().size());
        BigDecimal totalBalance = totalIncome.subtract(totalExpense);
        return DashboardDTO.builder()
                .userId(userId)
                .username(username)
                .totalIncome(totalIncome)
                .totalExpense(totalExpense)
                .totalBalance(totalBalance)
                .recentTransactions(recentTransactions.stream()
                        .map(TransactionMapper::toResponseDTO)
                        .toList())
                .build();
    }
}
