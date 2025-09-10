package com.cashy.cashy.budget.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class BudgetResponseDTO {
    private String budgetTitle;
    private double amountAllocated;
    private String description;
}
