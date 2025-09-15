package com.cashy.cashy.budget.controller;

import com.cashy.cashy.budget.dto.BudgetRequestDTO;
import com.cashy.cashy.budget.dto.BudgetResponseDTO;
import com.cashy.cashy.budget.model.Budget;
import com.cashy.cashy.budget.service.BudgetService;
import com.cashy.cashy.transaction.dto.TransactionResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users/{userId}/budgets")
public class BudgetController {
    private final BudgetService budgetService;

    @PostMapping
    public ResponseEntity<BudgetResponseDTO> createBudgetForUser(@PathVariable UUID userId,
            @RequestBody @Valid BudgetRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(budgetService.createBudget(requestDTO, userId));
    }

    @GetMapping
    public ResponseEntity<List<BudgetResponseDTO>> getAllBudgetsForUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(budgetService.getAllBudgetsForUser(userId));
    }

    @GetMapping("/{budgetId}")
    public ResponseEntity<List<TransactionResponseDTO>> getAllTransactionsForBudget(
            @PathVariable Long budgetId,
            @PathVariable UUID userId
    ) {
        return ResponseEntity.ok(budgetService.getAllTransactionsForBudget(userId, budgetId));
    }

    @PutMapping("/{budgetId}")
    public ResponseEntity<BudgetResponseDTO> updateBudgetForUser(
            @PathVariable UUID userId,
            @PathVariable Long budgetId,
            @RequestBody BudgetRequestDTO requestDTO) {
        return ResponseEntity.ok(budgetService.updateBudget(requestDTO, userId, budgetId));
    }

    @DeleteMapping("/{budgetId}")
    public ResponseEntity<Void> deleteBudgetForUser(@PathVariable UUID userId, @PathVariable Long budgetId) {
        budgetService.deleteBudget(budgetId, userId);
        return ResponseEntity.noContent().build();
    }
}
