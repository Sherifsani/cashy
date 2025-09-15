package com.cashy.cashy.transaction.service;

import com.cashy.cashy.auth.exception.TransactionNotFoundException;
import com.cashy.cashy.auth.model.UserProfile;
import com.cashy.cashy.auth.service.UserService;
import com.cashy.cashy.budget.exceptions.BudgetNotFoundException;
import com.cashy.cashy.budget.model.Budget;
import com.cashy.cashy.budget.service.BudgetService;
import com.cashy.cashy.category.model.Category;
import com.cashy.cashy.category.service.CategoryService;
import com.cashy.cashy.transaction.dto.TransactionRequestDTO;
import com.cashy.cashy.transaction.dto.TransactionResponseDTO;
import com.cashy.cashy.transaction.exception.UserNotFoundException;
import com.cashy.cashy.transaction.mapper.TransactionMapper;
import com.cashy.cashy.transaction.model.Transaction;
import com.cashy.cashy.transaction.model.TransactionType;
import com.cashy.cashy.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final UserService userService;
    private final CategoryService categoryService;
    private final BudgetService budgetService;

    // helper function to find a user
    public UserProfile findUserOrThrow(UUID userId) {
        return userService.findUserById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    }

    // creates a new transaction
    public TransactionResponseDTO createTransaction(TransactionRequestDTO requestDTO, UUID userId) {
        UserProfile user = findUserOrThrow(userId);
        Category category = categoryService.getCategoryById(requestDTO.getCategoryId());
        Transaction newTransaction = TransactionMapper.toEntity(requestDTO);
        newTransaction.setCategory(category);
        newTransaction.setUserProfile(user);
        if (requestDTO.getBudgetId() != null) {
            Budget budget = budgetService.getBudgetById(requestDTO.getBudgetId())
                    .orElseThrow(() -> new BudgetNotFoundException(requestDTO.getBudgetId()));
            budget.setAmountSpent(budget.getAmountSpent().add(requestDTO.getAmount()));
            budget.setBalance(budget.getAmountAllocated().subtract(budget.getAmountSpent()));
            newTransaction.setBudget(budget);
        }
        user.getTransactions().add(newTransaction);
        userService.saveUser(user);
        return TransactionMapper.toResponseDTO(newTransaction);
    }


    public Page<TransactionResponseDTO> getTransactionsByUserId(UUID userId, Pageable pageable) {
        UserProfile user = findUserOrThrow(userId);
        return transactionRepository.findByUserProfile(user, pageable)
                .map(TransactionMapper::toResponseDTO);
    }

    public Page<TransactionResponseDTO> getTransactionsByTypeForUser(UUID userId, TransactionType type, Pageable pageable) {
        return transactionRepository.findByUserProfileIdAndTransactionType(userId, type, pageable)
                .map(TransactionMapper::toResponseDTO);
    }
    public TransactionResponseDTO updateTransaction(Long transactionId, TransactionRequestDTO requestDTO, UUID userId) {
        // find the transaction for this user
        Transaction transaction = transactionRepository
                .findByUserProfileIdAndId(userId, transactionId)
                .orElseThrow(() -> new TransactionNotFoundException(transactionId));

        // update only provided fields
        if (requestDTO.getAmount() != null) {
            transaction.setAmount(requestDTO.getAmount());
        }
        if (requestDTO.getDescription() != null) {
            transaction.setDescription(requestDTO.getDescription());
        }
        if (requestDTO.getTransactionType() != null) {
            transaction.setTransactionType(requestDTO.getTransactionType());
        }

        // save updated transaction
        transactionRepository.save(transaction);

        return TransactionMapper.toResponseDTO(transaction);
    }

    public void deleteTransaction(Long transactionId, UUID userId) {
        UserProfile user = findUserOrThrow(userId);

        // Verify transaction exists and belongs to user
        Transaction transaction = user.getTransactions().stream()
                .filter(t -> t.getId().equals(transactionId))
                .findFirst()
                .orElseThrow(() -> new TransactionNotFoundException(transactionId));

        transactionRepository.delete(transaction);
    }

}
