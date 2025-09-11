package com.cashy.cashy.transaction.controller;

import com.cashy.cashy.transaction.dto.TransactionRequestDTO;
import com.cashy.cashy.transaction.dto.TransactionResponseDTO;
import com.cashy.cashy.transaction.model.TransactionType;
import com.cashy.cashy.transaction.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users/{userId}/transactions")
public class TransactionController {
    public final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponseDTO> createTransaction(
            @RequestBody @Valid TransactionRequestDTO requestDTO,
            @PathVariable UUID userId) {
        TransactionResponseDTO response = transactionService.createTransaction(requestDTO, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponseDTO>> getTransactions(
            @PathVariable UUID userId,
            @RequestParam(required = false) TransactionType transactionType) {
        if (transactionType == null) {
            return ResponseEntity.ok(transactionService.getTransactionsByUserId(userId));
        }
        return ResponseEntity.ok(transactionService.getTransactionsByTypeForUser(userId, transactionType));
    }

    @PutMapping("/{transactionId}")
    public ResponseEntity<TransactionResponseDTO> updateTransaction(
            @PathVariable Long transactionId,
            @PathVariable UUID userId,
            @RequestBody @Valid TransactionRequestDTO requestDTO) {
        return ResponseEntity.ok().body(transactionService.updateTransaction(transactionId, requestDTO, userId));
    }

    @DeleteMapping("/{transactionId}")
    public ResponseEntity<Void> deleteTransaction(
            @PathVariable UUID userId,
            @PathVariable Long transactionId) {
        transactionService.deleteTransaction(transactionId, userId);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

}
