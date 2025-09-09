package com.cashy.cashy.budget;

import com.cashy.cashy.transaction.model.Transaction;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private double amountAllocated;

    private double amountSpent;

    private String title;

    private String description;

//    private List<Transaction> transactions;

}
