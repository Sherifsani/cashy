package com.cashy.cashy.transaction.dto;

import com.cashy.cashy.transaction.model.TransactionType;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
public class TransactionRequestDTO {
    @NotBlank(message = "amount is required to create a transaction")
    private BigDecimal amount;

    private String description;

    @NotBlank(message = "transaction type should be income or expense")
    private TransactionType transactionType;

    @NotBlank(message = "user id is missing")
    private UUID userId;

    @NotBlank(message = "category id is missing")
    private UUID categoryId;
}
