package com.cashy.cashy.budget.model;

import com.cashy.cashy.auth.model.UserProfile;
import com.cashy.cashy.transaction.model.Transaction;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
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

    private double balance;

    private String title;

    private String description;

    private LocalDate fromDate;

    private LocalDate toDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserProfile userProfile;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "budget",orphanRemoval = true)
    private List<Transaction> transactions;

}
