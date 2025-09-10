package com.cashy.cashy.transaction.dto;

import com.cashy.cashy.transaction.model.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
public class TransactionRequestDTO {
    @NotNull(message = "amount is required to create a transaction")
    private BigDecimal amount;

    private String description;

    @NotNull(message = "transaction type should be income or expense")
    private TransactionType transactionType;

    @NotNull(message = "category id is missing")
    private UUID categoryId;

    private Long budgetId;
}
