package com.financeAI.ms_bank_sync.demo.model;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StatementNu {
    @CsvBindByName(column = "data")
    private String data;

    @CsvBindByName(column = "valor")
    private BigDecimal valor;

    @CsvBindByName(column = "identificador")
    private String identificacao;

    @CsvBindByName(column = "descrição")
    private String descricao;

}
