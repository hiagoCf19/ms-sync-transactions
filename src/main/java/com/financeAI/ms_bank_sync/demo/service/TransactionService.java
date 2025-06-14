package com.financeAI.ms_bank_sync.demo.service;

import com.financeAI.ms_bank_sync.demo.entity.Transaction;
import com.financeAI.ms_bank_sync.demo.mapper.TransactionMapper;
import com.financeAI.ms_bank_sync.demo.model.StatementNu;
import com.financeAI.ms_bank_sync.demo.parser.CsvStatementParser;
import com.financeAI.ms_bank_sync.demo.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private CsvStatementParser parser;
    @Autowired
    private TransactionMapper transactionMapper;


    public void uploadFile(MultipartFile file, String userId) {
        try {
            // Etapa 1: parse
            List<StatementNu> registros = parser.parse(file);

            // Etapa 2: mapeia para transactions (aqui já valida também)
            List<Transaction> transactions = registros.stream()
                    .map(nu -> transactionMapper.fillTransaction(nu, userId))
                    .toList();

            transactionRepository.saveAll(transactions);

        } catch (Exception e) {
            throw new RuntimeException("Falha ao importar arquivo. Nenhuma transação foi salva.", e);
        }
    }


}
