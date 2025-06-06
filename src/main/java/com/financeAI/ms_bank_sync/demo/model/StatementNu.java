package com.financeAI.ms_bank_sync.demo.model;

import com.opencsv.bean.CsvBindByName;

import java.math.BigDecimal;

public class StatementNu {
    @CsvBindByName(column = "data")
    private String data;

    @CsvBindByName(column = "valor")
    private BigDecimal valor;

    @CsvBindByName(column = "identificador")
    private String identificacao;

    @CsvBindByName(column = "descrição")
    private String descricao;

    // Getters e setters
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getIdentificacao() {
        return identificacao;
    }

    public void setIdentificacao(String identificacao) {
        this.identificacao = identificacao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
