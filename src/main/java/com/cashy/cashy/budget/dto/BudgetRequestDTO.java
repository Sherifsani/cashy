package com.cashy.cashy.budget.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Getter
@Setter
public class BudgetRequestDTO {
    @NotNull(message = "enter an amount")
    private BigDecimal amountAllocated;

    @NotNull(message = "title is required")
    private String title;

    private String description;

    @NotNull(message = "start date is required")
    private LocalDate fromDate;

    @NotNull(message = "end date is required")
    private LocalDate toDate;
}
