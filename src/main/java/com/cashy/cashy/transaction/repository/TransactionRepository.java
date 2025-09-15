package com.cashy.cashy.transaction.repository;

import com.cashy.cashy.auth.model.UserProfile;
import com.cashy.cashy.transaction.model.Transaction;
import com.cashy.cashy.transaction.model.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Page<Transaction> findByUserProfile(UserProfile user, Pageable pageable);
    Page<Transaction> findByUserProfileIdAndTransactionType(UUID userId, TransactionType type, Pageable pageable);
    Optional<Transaction> findByUserProfileIdAndId(UUID id, Long transactionId);
}
