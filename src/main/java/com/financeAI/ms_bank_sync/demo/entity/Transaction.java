package com.financeAI.ms_bank_sync.demo.entity;

import com.financeAI.ms_bank_sync.demo.enums.TransactionCategory;
import com.financeAI.ms_bank_sync.demo.enums.TransactionPaymentMethod;
import com.financeAI.ms_bank_sync.demo.enums.TransactionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction")
@Getter
@Setter
public class Transaction {

    @Id
    @Column(nullable = false, updatable = false)
    private String id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionCategory category;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private TransactionPaymentMethod paymentMethod;

    @Column(nullable = false)
    private LocalDate date;

    @Column(name= "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name= "updated_at",nullable = false)
    private LocalDateTime updatedAt;

    @Column(name= "user_id",nullable = false)
    private String userId;



}

