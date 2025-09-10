package com.cashy.cashy.budget.controller;

import com.cashy.cashy.budget.dto.BudgetRequestDTO;
import com.cashy.cashy.budget.dto.BudgetResponseDTO;
import com.cashy.cashy.budget.model.Budget;
import com.cashy.cashy.budget.service.BudgetService;
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
    public ResponseEntity<BudgetResponseDTO> createBudget(@PathVariable UUID userId, @RequestBody BudgetRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(budgetService.createBudget(requestDTO,userId));
    }

    @GetMapping
    public ResponseEntity<List<BudgetResponseDTO>> getAllBudgets(@PathVariable UUID userId) {
        return ResponseEntity.ok(budgetService.getAllBudgetsForUser(userId));
    }

    @DeleteMapping("/{budgetId}")
    public ResponseEntity<String> deleteBudget(@PathVariable UUID userId, @PathVariable Long budgetId) {
        budgetService.deleteBudget(budgetId, userId);
        return ResponseEntity.ok("Budget deleted successfully");
    }
}
