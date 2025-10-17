package com.cashy.cashy.budget.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Getter
@Setter
public class BudgetResponseDTO {
    private Long id;
    private String budgetTitle;
    private String description;
    private BigDecimal amountAllocated;
    private BigDecimal amountSpent;
    private BigDecimal balance;
    private LocalDate fromDate;
    private LocalDate toDate;
}
