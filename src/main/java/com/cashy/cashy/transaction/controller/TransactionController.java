package com.cashy.cashy.transaction.controller;

import com.cashy.cashy.transaction.dto.TransactionRequestDTO;
import com.cashy.cashy.transaction.dto.TransactionResponseDTO;
import com.cashy.cashy.transaction.model.TransactionType;
import com.cashy.cashy.transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users/{userId}/transactions")
public class TransactionController {
    public final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponseDTO> createTransaction(
            @RequestBody TransactionRequestDTO requestDTO,
            @PathVariable UUID userId
    ){
        requestDTO.setUserId(userId);
        TransactionResponseDTO response = transactionService.createTransaction(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponseDTO>> getTransactions(
            @PathVariable UUID userId,
            @RequestParam(required = false) TransactionType transactionType
            ){
        if(transactionType == null){
            return ResponseEntity.ok(transactionService.getTransactionsByUserId(userId));
        }
        return ResponseEntity.ok(transactionService.getTransactionsByTypeForUser(userId, transactionType));
    }

    @PutMapping("/{transactionId}")
    public ResponseEntity<TransactionResponseDTO> updateTransaction(
            @PathVariable Long transactionId,
            @PathVariable UUID userId,
            @RequestBody TransactionRequestDTO requestDTO
    ){
        requestDTO.setUserId(userId);
        return ResponseEntity.ok().body(transactionService.updateTransaction(transactionId,requestDTO));
    }


}
