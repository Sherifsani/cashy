package com.cashy.cashy.budget.dto;

import com.cashy.cashy.budget.model.Budget;

public class BudgetMapper {
    public static BudgetResponseDTO toDTO(Budget budget) {
        return BudgetResponseDTO.builder()
                .id(budget.getId())
                .budgetTitle(budget.getTitle())
                .description(budget.getDescription())
                .amountAllocated(budget.getAmountAllocated())
                .amountSpent(budget.getAmountSpent())
                .balance(budget.getBalance())
                .fromDate(budget.getFromDate())
                .toDate(budget.getToDate())
                .build();
    }

    public static Budget toEntity(BudgetRequestDTO requestDTO) {
        return Budget.builder()
                .title(requestDTO.getTitle())
                .description(requestDTO.getDescription())
                .amountAllocated(requestDTO.getAmountAllocated())
                .fromDate(requestDTO.getFromDate())
                .toDate(requestDTO.getToDate())
                .build();
    }
}
