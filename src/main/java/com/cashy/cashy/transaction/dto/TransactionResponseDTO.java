package com.cashy.cashy.transaction.dto;

import com.cashy.cashy.transaction.model.TransactionType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Getter
@Setter
public class TransactionResponseDTO {
    private Long id;
    private BigDecimal amount;
    private String description;
    private TransactionType transactionType;
    private String categoryName;
    private LocalDate transactionDate;
}
