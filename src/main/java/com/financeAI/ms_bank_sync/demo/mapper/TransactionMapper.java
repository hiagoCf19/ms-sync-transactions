package com.financeAI.ms_bank_sync.demo.mapper;

import com.financeAI.ms_bank_sync.demo.entity.Transaction;
import com.financeAI.ms_bank_sync.demo.enums.TransactionPaymentMethod;
import com.financeAI.ms_bank_sync.demo.enums.TransactionType;
import com.financeAI.ms_bank_sync.demo.model.StatementNu;
import com.financeAI.ms_bank_sync.demo.service.GeminiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class TransactionMapper {
    @Autowired
    private GeminiService geminiService;

    public Transaction fillTransaction(StatementNu nu, String userid){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate data = LocalDate.parse(nu.getData(), formatter);
        TransactionCategoryName categoryAndName= determineTransactionCategoryAndName(nu);
        Transaction transaction= new Transaction();
        transaction.setType(determineTransactionType(nu));
        transaction.setAmount(nu.getValor());
        transaction.setDate(data);
        transaction.setId(nu.getIdentificacao());
        transaction.setName(categoryAndName.name());
        transaction.setCategory(categoryAndName.category());
        transaction.setPaymentMethod(determinePaymentMethod(nu));
        transaction.setUpdatedAt(LocalDateTime.now());
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setUserId(userid);
        return transaction;

    }
    private TransactionType determineTransactionType(StatementNu nu){

        if(nu.getValor().compareTo(BigDecimal.ZERO) < 0){
            return TransactionType.EXPENSE;
        }else{
            return TransactionType.DEPOSIT;
        }
    }

    private TransactionCategoryName determineTransactionCategoryAndName(StatementNu nu){
        //TODO:LOGICA PARA DETERMINAR A CATEGORIA
        return geminiService.iaGenerateCategoryAndName(nu.getDescricao());
    }
    private TransactionPaymentMethod determinePaymentMethod(StatementNu nu){
        String data= nu.getDescricao();
        if(data.contains("Pix")){
            return TransactionPaymentMethod.PIX;
        }
        if(data.contains("débito")){
            return TransactionPaymentMethod.DEBIT_CARD;
        }
        if(data.contains("GRUPO IVORY")){
            return  TransactionPaymentMethod.BANK_TRANSFER;
        }
        if(data.contains("boleto")){
            return TransactionPaymentMethod.BANK_SLIP;
        }
        return TransactionPaymentMethod.OTHER;
    }


}
