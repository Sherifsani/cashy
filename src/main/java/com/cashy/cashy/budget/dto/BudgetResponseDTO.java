package com.cashy.cashy.budget.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
public class BudgetResponseDTO {
    private String budgetTitle;
    private BigDecimal amountAllocated;
    private String description;
}
