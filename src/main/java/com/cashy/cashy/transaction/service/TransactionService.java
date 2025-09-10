package com.cashy.cashy.transaction.service;

import com.cashy.cashy.auth.exception.TransactionNotFound;
import com.cashy.cashy.auth.model.UserProfile;
import com.cashy.cashy.auth.service.UserService;
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

//    helper function to find a user
    public Optional<UserProfile> findUserOrThrow(UUID userId){
        return userService.findUserById(userId);
    }

//    creates a new transaction
    public TransactionResponseDTO createTransaction(TransactionRequestDTO requestDTO, UUID userId){
        UserProfile user = findUserOrThrow(userId).get();
        Category category = categoryService.getCategoryById(requestDTO.getCategoryId());
        Transaction newTransaction = TransactionMapper.toEntity(requestDTO);
        newTransaction.setCategory(category);
        newTransaction.setUserProfile(user);
        user.getTransactions().add(newTransaction);
        userService.saveUser(user);
        return TransactionMapper.toResponseDTO(newTransaction);
    }

//    fetch all transactions for a particular user
    public List<TransactionResponseDTO> getTransactionsByUserId(UUID userId){
        UserProfile user = findUserOrThrow(userId).get();

        return user
                .getTransactions()
                .stream()
                .map(TransactionMapper::toResponseDTO)
                .toList();
    }

//    fetch transactions for each user by transaction type
    public List<TransactionResponseDTO> getTransactionsByTypeForUser(UUID userId, TransactionType type){
        UserProfile user = findUserOrThrow(userId).get();
        return user
                .getTransactions()
                .stream()
                .filter(transaction -> transaction.getTransactionType() == type)
                .map(TransactionMapper::toResponseDTO)
                .toList();
    }

    public TransactionResponseDTO updateTransaction(Long transactionId, TransactionRequestDTO requestDTO, UUID userId) {
        // find the user or throw exception
        Optional<UserProfile> user = findUserOrThrow(userId);

        // find the transaction for this user
        Transaction transaction = transactionRepository
                .findByUserProfileIdAndId(userId, transactionId)
                .orElseThrow(() -> new RuntimeException("transaction not found"));

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
        UserProfile user = findUserOrThrow(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        Transaction transaction = user.getTransactions().stream()
                .filter(t -> t.getId().equals(transactionId))
                .findFirst()
                .orElseThrow(() -> new TransactionNotFound(transactionId));

        transactionRepository.deleteById(transactionId);
    }

}
