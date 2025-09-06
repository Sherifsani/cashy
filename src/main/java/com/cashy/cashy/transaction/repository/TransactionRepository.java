package com.cashy.cashy.transaction.repository;

import com.cashy.cashy.auth.model.UserProfile;
import com.cashy.cashy.transaction.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUserProfile(UserProfile userProfile);
    List<Transaction> findByUserProfileId(UUID id);
}
