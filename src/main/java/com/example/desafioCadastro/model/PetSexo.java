package com.example.desafioCadastro.model;

public enum PetSexo {
    MACHO("MACHO"),
    FEMEA("FEMEA");

    private String petSexo;

    PetSexo(String petSexo) {
        this.petSexo = petSexo;
    }

    @Override
    public String toString() {
        return petSexo;
    }
}
