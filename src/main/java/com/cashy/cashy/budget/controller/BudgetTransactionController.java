package com.cashy.cashy.budget.controller;

import com.cashy.cashy.budget.service.BudgetTransactionService;
import com.cashy.cashy.transaction.dto.TransactionResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users/{userId}/budgets/{budgetId}/transactions")
public class BudgetTransactionController {
    private final BudgetTransactionService budgetTransactionService;

    @PostMapping("/{transactionId}")
    public ResponseEntity<TransactionResponseDTO> addTransactionToBudget(
            @PathVariable UUID userId,
            @PathVariable Long budgetId,
            @PathVariable Long transactionId) {
        TransactionResponseDTO response = budgetTransactionService.addTransactionToBudget(userId, budgetId, transactionId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{transactionId}")
    public ResponseEntity<Void> removeTransactionFromBudget(
            @PathVariable UUID userId,
            @PathVariable Long budgetId,
            @PathVariable Long transactionId) {
        budgetTransactionService.removeTransactionFromBudget(userId, budgetId, transactionId);
        return ResponseEntity.noContent().build();
    }
}
