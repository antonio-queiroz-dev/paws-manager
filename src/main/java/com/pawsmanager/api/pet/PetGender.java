package com.pawsmanager.api.pet;

public enum PetGender {
    MACHO("MACHO"),
    FEMEA("FEMEA");

    private String petSexo;

    PetGender(String petSexo) {
        this.petSexo = petSexo;
    }

    @Override
    public String toString() {
        return petSexo;
    }
}
