package com.example.desafioCadastro.model;

import jakarta.persistence.*;

@Entity
@Table(name = "perguntas")
public class Pergunta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String descicao;

    public Pergunta() {
    }

    public Pergunta( String descicao) {
        this.descicao = descicao;
    }

    public long getId() {
        return id;
    }

    public String getDescicao() {
        return descicao;
    }

    public void setDescicao(String descicao) {
        this.descicao = descicao;
    }
}
