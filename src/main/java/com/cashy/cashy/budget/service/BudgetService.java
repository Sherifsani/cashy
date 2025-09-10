package com.cashy.cashy.budget.service;

import com.cashy.cashy.auth.model.UserProfile;
import com.cashy.cashy.auth.service.UserService;
import com.cashy.cashy.budget.dto.BudgetMapper;
import com.cashy.cashy.budget.dto.BudgetRequestDTO;
import com.cashy.cashy.budget.dto.BudgetResponseDTO;
import com.cashy.cashy.budget.exceptions.BudgetNotFoundException;
import com.cashy.cashy.budget.model.Budget;
import com.cashy.cashy.budget.repository.BudgetRepository;
import com.cashy.cashy.transaction.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BudgetService {
    private final BudgetRepository budgetRepository;
    private final UserService userService;

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
                .amountSpent(0.0)
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

    public void deleteBudget(Long budgetId, UUID userId) {
        if (budgetId == null || userId == null) {
            throw new IllegalArgumentException("Budget ID and User ID cannot be null");
        }

        // Check if user exists
        UserProfile user = userService.findUserById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        Budget budget = user
                .getBudgets()
                .stream()
                .filter(b -> b.getId().equals(budgetId))
                .findAny()
                .orElseThrow(() -> new BudgetNotFoundException(budgetId));

        budgetRepository.delete(budget);
    }
}
