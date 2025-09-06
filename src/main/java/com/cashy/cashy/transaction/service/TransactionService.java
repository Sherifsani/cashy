package com.cashy.cashy.transaction.service;

import com.cashy.cashy.auth.model.UserProfile;
import com.cashy.cashy.auth.service.UserService;
import com.cashy.cashy.transaction.dto.TransactionRequestDTO;
import com.cashy.cashy.transaction.dto.TransactionResponseDTO;
import com.cashy.cashy.transaction.exception.UserNotFoundException;
import com.cashy.cashy.transaction.mapper.TransactionMapper;
import com.cashy.cashy.transaction.model.Transaction;
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

//    creates a new transaction
    public TransactionResponseDTO createTransaction(TransactionRequestDTO requestDTO){
        Optional<UserProfile> userProfile = userService.findUserById(requestDTO.getUserId());
        if (userProfile.isEmpty()){
            throw new UserNotFoundException(requestDTO.getUserId());
        }
        UserProfile user = userProfile.get();
        Transaction newTransaction = TransactionMapper.toEntity(requestDTO);
        newTransaction.setUserProfile(user);
        user.getTransactions().add(newTransaction);
        userService.saveUser(user);
        return TransactionMapper.toResponseDTO(newTransaction);
    }

//    fetch all transactions for a particular user
    public List<TransactionResponseDTO> getTransactionsByUserId(UUID userId){
        Optional<UserProfile> userProfile = userService.findUserById(userId);
        if (userProfile.isEmpty()){
            throw new UserNotFoundException(userId);
        }
        UserProfile user = userProfile.get();

        return user
                .getTransactions()
                .stream()
                .map(TransactionMapper::toResponseDTO)
                .toList();
    }

//    public boolean deleteTransaction(Long id){
//        if(transactionRepository.existsById(id)){
//            return false;
//        }
//        return true;
//    }
}
