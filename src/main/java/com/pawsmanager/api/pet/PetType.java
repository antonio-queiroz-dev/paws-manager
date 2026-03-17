package com.pawsmanager.api.pet;

public enum PetType {
    CACHORRO("CACHORRO"),
    GATO("GATO");

    private final String petTipo;

    PetType(String petTipo) {
        this.petTipo = petTipo;
    }

    @Override
    public String toString() {
        return  petTipo;

    }
}
