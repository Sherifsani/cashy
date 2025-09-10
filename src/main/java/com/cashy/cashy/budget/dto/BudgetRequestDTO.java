package com.cashy.cashy.budget.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Builder
@Getter
@Setter
public class BudgetRequestDTO {
    private double amountAllocated;

    private String title;

    private String description;

    private LocalDate fromDate;

    private LocalDate toDate;
}
