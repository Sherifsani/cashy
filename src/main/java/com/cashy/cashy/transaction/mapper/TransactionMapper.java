package com.cashy.cashy.transaction.mapper;

import com.cashy.cashy.transaction.dto.TransactionRequestDTO;
import com.cashy.cashy.transaction.dto.TransactionResponseDTO;
import com.cashy.cashy.transaction.model.Transaction;

import java.time.LocalDateTime;

public class TransactionMapper {
    public static Transaction toEntity(TransactionRequestDTO requestDTO) {
        return Transaction.builder()
                .amount(requestDTO.getAmount())
                .description(requestDTO.getDescription())
                .transactionType(requestDTO.getTransactionType())
                .transactionType(requestDTO.getTransactionType())
                .build();
    }

    public static TransactionResponseDTO toResponseDTO(Transaction transaction) {
        return TransactionResponseDTO.builder()
                .id(transaction.getId())
                .description(transaction.getDescription())
                .amount(transaction.getAmount())
                .transactionType(transaction.getTransactionType())
                .categoryName(transaction.getCategory().getCategoryName())
                .build();
    }
}
