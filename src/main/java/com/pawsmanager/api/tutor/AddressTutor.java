package com.pawsmanager.api.tutor;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Embeddable;

@Embeddable
public class AddressTutor {

    @Schema(example = "123")
    private String numeroCasa;
    @Schema(example = "São Paulo")
    private String cidade;
    @Schema(example = "Rua das Flores")
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
