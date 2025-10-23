package com.example.desafioCadastro.model;

public enum PetTipo {
    CACHORRO("Cachorro"),
    GATO("Gato");

    private final String petTipo;

    PetTipo(String petTipo) {
        this.petTipo = petTipo;
    }

    @Override
    public String toString() {
        return  petTipo;

    }
}
