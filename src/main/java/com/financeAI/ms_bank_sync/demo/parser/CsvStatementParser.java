package com.financeAI.ms_bank_sync.demo.parser;

import com.financeAI.ms_bank_sync.demo.model.StatementNu;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

@Component
public class CsvStatementParser {
    public List<StatementNu> parse(MultipartFile file) {
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            CsvToBean<StatementNu> csvToBean = new CsvToBeanBuilder<StatementNu>(reader)
                    .withType(StatementNu.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            return csvToBean.parse();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao ler CSV", e);
        }
    }
}
