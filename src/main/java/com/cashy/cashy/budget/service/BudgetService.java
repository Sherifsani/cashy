package com.cashy.cashy.budget.service;

import com.cashy.cashy.auth.model.UserProfile;
import com.cashy.cashy.auth.service.UserService;
import com.cashy.cashy.budget.dto.BudgetMapper;
import com.cashy.cashy.budget.dto.BudgetRequestDTO;
import com.cashy.cashy.budget.dto.BudgetResponseDTO;
import com.cashy.cashy.budget.exceptions.BudgetNotFoundException;
import com.cashy.cashy.budget.model.Budget;
import com.cashy.cashy.budget.repository.BudgetRepository;
import com.cashy.cashy.transaction.dto.TransactionResponseDTO;
import com.cashy.cashy.transaction.exception.UserNotFoundException;
import com.cashy.cashy.transaction.mapper.TransactionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BudgetService {
    private final BudgetRepository budgetRepository;
    private final UserService userService;

    public void validateArgument(Long budgetId, UUID userId){
        if (budgetId == null || userId == null) {
            throw new IllegalArgumentException("Budget ID and User ID cannot be null");
        }
    }

    public BudgetResponseDTO createBudget(BudgetRequestDTO budgetRequestDTO, UUID userId) {

        Optional<UserProfile> optionalUserProfile = userService.findUserById(userId);
        if(optionalUserProfile.isEmpty()){
            throw new UserNotFoundException(userId);
        }
        Budget newBudget = Budget.builder()
                .title(budgetRequestDTO.getTitle())
                .description(budgetRequestDTO.getDescription())
                .amountAllocated(budgetRequestDTO.getAmountAllocated())
                .userProfile(optionalUserProfile.get())
                .fromDate(budgetRequestDTO.getFromDate())
                .toDate(budgetRequestDTO.getToDate())
                .amountSpent(new BigDecimal("0.0"))
                .balance(budgetRequestDTO.getAmountAllocated())
                .build();
        budgetRepository.save(newBudget);
        return BudgetMapper.toDTO(newBudget);
    }

    public List<BudgetResponseDTO> getAllBudgetsForUser(UUID userId) {
        Optional<UserProfile> optionalUserProfile = userService.findUserById(userId);
        if(optionalUserProfile.isEmpty()){
            throw new UserNotFoundException(userId);
        }
        UserProfile user = optionalUserProfile.get();
        List<Budget> budgets = user.getBudgets();
        return budgets.stream().map(BudgetMapper::toDTO).collect(Collectors.toList());
    }

    public Optional<Budget> getBudgetById(Long budgetId) {
        return budgetRepository.findById(budgetId);
    }

    public void deleteBudget(Long budgetId, UUID userId) {
        validateArgument(budgetId, userId);

        // Check if user exists
        UserProfile user = userService.findUserById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        Budget budget = budgetRepository
                .findByIdAndUserProfile_Id(budgetId, userId)
                .orElseThrow(() -> new BudgetNotFoundException(budgetId));

        budgetRepository.delete(budget);
    }

    public BudgetResponseDTO updateBudget(BudgetRequestDTO budgetRequestDTO, UUID userId, Long budgetId) {
        validateArgument(budgetId, userId);

        // Check if user exists
        UserProfile user = userService.findUserById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        Budget budget = budgetRepository
                .findByIdAndUserProfile_Id(budgetId, userId)
                .orElseThrow(() -> new BudgetNotFoundException(budgetId));


        if(budgetRequestDTO.getAmountAllocated().compareTo(budget.getAmountSpent()) < 0) {
            throw new IllegalArgumentException("Allocated amount cannot be less than the amount already spent.");
        }
        if(budgetRequestDTO.getTitle() != null){
            budget.setTitle(budgetRequestDTO.getTitle());
        }
        if(budgetRequestDTO.getDescription() != null){
            budget.setDescription(budgetRequestDTO.getDescription());
        }
        if(budgetRequestDTO.getFromDate() != null){
            budget.setFromDate(budgetRequestDTO.getFromDate());
        }
        if(budgetRequestDTO.getToDate() != null){
            budget.setToDate(budgetRequestDTO.getToDate());
        }
        if(budgetRequestDTO.getAmountAllocated() != null){
            budget.setAmountAllocated(budgetRequestDTO.getAmountAllocated());
        }

        // Recalculate balance
        budget.setBalance(budget.getAmountAllocated().subtract(budget.getAmountSpent()));
        budgetRepository.save(budget);
        return BudgetMapper.toDTO(budget);
    }

//    fetch transactions for each budget for user
    public List<TransactionResponseDTO> getAllTransactionsForBudget(UUID userId, Long budgetId){
        validateArgument(budgetId, userId);

        // Check if user exists
        UserProfile user = userService.findUserById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        Budget budget = budgetRepository
                .findByIdAndUserProfile_Id(budgetId, userId)
                .orElseThrow(() -> new BudgetNotFoundException(budgetId));

        return budget.getTransactions()
                .stream()
                .map(TransactionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}
