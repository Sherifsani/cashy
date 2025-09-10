package com.cashy.cashy.budget.repository;

import com.cashy.cashy.budget.model.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BudgetRepository extends JpaRepository<Budget, Long> {

}
