package com.cashy.cashy.transaction.controller;

import com.cashy.cashy.transaction.dto.TransactionRequestDTO;
import com.cashy.cashy.transaction.dto.TransactionResponseDTO;
import com.cashy.cashy.transaction.model.TransactionType;
import com.cashy.cashy.transaction.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
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
    public ResponseEntity<Page<TransactionResponseDTO>> getTransactions(
            @PathVariable UUID userId,
            @RequestParam(required = false) TransactionType transactionType,
            Pageable pageable) {

        if (transactionType == null) {
            return ResponseEntity.ok(transactionService.getTransactionsByUserId(userId, pageable));
        }
        return ResponseEntity.ok(transactionService.getTransactionsByTypeForUser(userId, transactionType, pageable));
    }

    @PutMapping("/{transactionId}")
    public ResponseEntity<TransactionResponseDTO> updateTransaction(
            @PathVariable Long transactionId,
            @PathVariable UUID userId,
            @RequestBody @Valid TransactionRequestDTO requestDTO) {
        return ResponseEntity.ok().body(transactionService.updateTransaction(transactionId, requestDTO, userId));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<TransactionResponseDTO>> searchTransactions(
            @PathVariable UUID userId,
            @RequestParam String query,
            Pageable pageable) {
        return ResponseEntity.ok(transactionService.searchTransactions(userId, query, pageable));
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<TransactionResponseDTO>> filterTransactions(
            @PathVariable UUID userId,
            @RequestParam(required = false) LocalDate fromDate,
            @RequestParam(required = false) LocalDate toDate,
            @RequestParam(required = false) TransactionType type,
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(required = false) BigDecimal minAmount,
            @RequestParam(required = false) BigDecimal maxAmount,
            Pageable pageable) {
        return ResponseEntity.ok(transactionService.filterTransactions(
                userId, fromDate, toDate, type, categoryId, minAmount, maxAmount, pageable));
    }

    @DeleteMapping("/{transactionId}")
    public ResponseEntity<Void> deleteTransaction(
            @PathVariable UUID userId,
            @PathVariable Long transactionId) {
        transactionService.deleteTransaction(transactionId, userId);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

}
