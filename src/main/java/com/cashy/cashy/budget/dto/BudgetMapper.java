package com.cashy.cashy.budget.dto;

import com.cashy.cashy.budget.model.Budget;

public class BudgetMapper {
    public static BudgetResponseDTO toDTO(Budget budget) {
        return BudgetResponseDTO.builder()
                .budgetTitle(budget.getTitle())
                .description(budget.getDescription())
                .amountAllocated(budget.getAmountAllocated())
                .build();
    }

    public static Budget toEntity(BudgetRequestDTO requestDTO) {
        return Budget.builder().build();
    }
}
