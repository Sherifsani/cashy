package com.cashy.cashy.dashboard.dto;

import com.cashy.cashy.transaction.dto.TransactionResponseDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
public class DashboardDTO {
    private UUID userId;
    private String username;
//    private BigDecimal totalIncome;
//    private BigDecimal totalExpense;
//    private BigDecimal totalBalance;
//    private List<TransactionResponseDTO> recentTransactions;
//    private Integer totalBudgetUsage;
}
