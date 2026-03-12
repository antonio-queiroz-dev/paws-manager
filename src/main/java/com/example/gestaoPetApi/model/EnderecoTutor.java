package com.example.gestaoPetApi.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class EnderecoTutor {

    private String numeroCasa;
    private String cidade;
    private String rua;

    public String getNumeroCasa() {
        return numeroCasa;
    }

    public void setNumeroCasa(String numeroCasa) {
        this.numeroCasa = numeroCasa;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }
}
