package com.example.gestaoPetApi.model;

public enum PetTipo {
    CACHORRO("CACHORRO"),
    GATO("GATO");

    private final String petTipo;

    PetTipo(String petTipo) {
        this.petTipo = petTipo;
    }

    @Override
    public String toString() {
        return  petTipo;

    }
}
