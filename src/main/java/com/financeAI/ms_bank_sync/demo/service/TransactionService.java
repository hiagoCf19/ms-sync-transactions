package com.financeAI.ms_bank_sync.demo.service;

import com.financeAI.ms_bank_sync.demo.entity.Transaction;
import com.financeAI.ms_bank_sync.demo.model.StatementNu;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

@Service
public class TransactionService {
    public void uploadFile(MultipartFile file){
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            CsvToBean<StatementNu> csvToBean = new CsvToBeanBuilder<StatementNu>(reader)
                    .withType(StatementNu.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            List<StatementNu> registros = csvToBean.parse();

            for (StatementNu record : registros) {
                System.out.println(
                        String.format("data: %s, valor: %s, identificador: %s, descrição: %s",
                                record.getData(),
                                record.getValor(),
                                record.getIdentificacao(),
                                record.getDescricao()
                        )
                );
                Transaction transaction= new Transaction();
                transaction.setId("");
                transaction.setName("");
                transaction.getType();
                transaction.setAmount();
                transaction.setCategory();
                transaction.setPaymentMethod();
                transaction.setDate();
                transaction.setCreatedAt();
                transaction.setUpdatedAt();
                transaction.setUserId();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
