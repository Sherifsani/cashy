package com.cashy.cashy.auth.model;

import com.cashy.cashy.category.model.Category;
import com.cashy.cashy.transaction.model.Transaction;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Builder
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userProfile", orphanRemoval = true)
    private List<Transaction> transactions = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userProfile", orphanRemoval = true)
    private List<Category> categories = new ArrayList<>();

/*    TODO: add feature for activation via email after registration
    private boolean isActive;
    private String activationCode;*/
}
