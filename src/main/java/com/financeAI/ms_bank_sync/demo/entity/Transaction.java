package com.financeAI.ms_bank_sync.demo.entity;

import com.financeAI.ms_bank_sync.demo.enums.TransactionCategory;
import com.financeAI.ms_bank_sync.demo.enums.TransactionPaymentMethod;
import com.financeAI.ms_bank_sync.demo.model.StatementNu;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Transaction")
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
    private TransactionCategory type;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionCategory category;

    @Enumerated(EnumType.STRING)
    @Column(name = "paymentMethod", nullable = false)
    private TransactionPaymentMethod paymentMethod;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private String userId;

    public Transaction buildNubank(String userId, StatementNu statementNu){
        Transaction transaction= new Transaction();
        transaction.setId(statementNu.getIdentificacao());
        transaction.setName();
        transaction.setType();
        transaction.setAmount();
        transaction.setCategory();
        transaction.setPaymentMethod();
        transaction.setDate();
        transaction.setCreatedAt();
        transaction.setUpdatedAt();
        transaction.setUserId();
        return transaction;
    }

}

