package com.financeAI.ms_bank_sync.demo.repository;

import com.financeAI.ms_bank_sync.demo.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {
}
