package com.cashy.cashy.budget.service;

import com.cashy.cashy.auth.model.UserProfile;
import com.cashy.cashy.auth.service.UserService;
import com.cashy.cashy.budget.exceptions.BudgetNotFoundException;
import com.cashy.cashy.budget.model.Budget;
import com.cashy.cashy.budget.repository.BudgetRepository;
import com.cashy.cashy.transaction.exception.UserNotFoundException;
import com.cashy.cashy.transaction.model.Transaction;
import com.cashy.cashy.transaction.service.TransactionService;
import com.cashy.cashy.transaction.dto.TransactionResponseDTO;
import com.cashy.cashy.transaction.mapper.TransactionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BudgetTransactionService {
    
    private final BudgetRepository budgetRepository;
    private final TransactionService transactionService;
    private final UserService userService;

    public TransactionResponseDTO addTransactionToBudget(UUID userId, Long budgetId, Long transactionId) {
        // Validate user exists
        UserProfile user = userService.findUserById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        // Get budget
        Budget budget = budgetRepository.findByIdAndUserProfile_Id(budgetId, userId)
                .orElseThrow(() -> new BudgetNotFoundException(budgetId));

        // Get transaction
        Transaction transaction = transactionService.getTransactionById(transactionId, userId);

        // Add transaction to budget
        budget.getTransactions().add(transaction);
        transaction.setBudget(budget);

        // Update budget spent amount
        BigDecimal newAmountSpent = budget.getAmountSpent().add(transaction.getAmount());
        budget.setAmountSpent(newAmountSpent);
        budget.setBalance(budget.getAmountAllocated().subtract(newAmountSpent));

        budgetRepository.save(budget);
        
        return TransactionMapper.toResponseDTO(transaction);
    }

    public void removeTransactionFromBudget(UUID userId, Long budgetId, Long transactionId) {
        // Validate user exists
        UserProfile user = userService.findUserById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        // Get budget
        Budget budget = budgetRepository.findByIdAndUserProfile_Id(budgetId, userId)
                .orElseThrow(() -> new BudgetNotFoundException(budgetId));

        // Get transaction
        Transaction transaction = transactionService.getTransactionById(transactionId, userId);

        // Remove transaction from budget
        budget.getTransactions().remove(transaction);
        transaction.setBudget(null);

        // Update budget spent amount
        BigDecimal newAmountSpent = budget.getAmountSpent().subtract(transaction.getAmount());
        budget.setAmountSpent(newAmountSpent);
        budget.setBalance(budget.getAmountAllocated().subtract(newAmountSpent));

        budgetRepository.save(budget);
    }
}
