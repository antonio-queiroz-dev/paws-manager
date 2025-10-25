package com.example.desafioCadastro.utils;

import com.example.desafioCadastro.model.Pet;
import org.springframework.stereotype.Component;

@Component
public class PetValidator {

    public static final String NAO_INFORMADO = "NAO INFORMADO";

    public void validar(Pet pet) {
        validarNome(pet);
        validarIdade(pet);
        validarPeso(pet);
        validarRaca(pet);
    }

    private void validarNome(Pet pet) {
        String nome = pet.getNomePet();

        if (nome == null || nome.isBlank()) {
            pet.setNomePet(NAO_INFORMADO);
            return;
        }

        String[] partes = nome.trim().split("\\s+");
        if (partes.length < 2) {
            throw new IllegalArgumentException("Precisa ter nome e sobrenome");
        }

        if (!nome.matches("^[A-Za-zÀ-ÿ ]+$")) {
            throw new IllegalArgumentException("Nome não pode conter números ou caracteres especiais");
        }
    }

    private void validarIdade(Pet pet) {
        String idade = pet.getIdade();

        if (idade == null || idade.isBlank()) {
            pet.setIdade(NAO_INFORMADO);
            return;
        }

        if (!idade.matches("^[0-9.,]+$")) {
            throw new IllegalArgumentException("Sómente números são validos");
        }

        double idadeRecebida = Double.parseDouble(idade.replace(",", "."));
        if (idadeRecebida > 20) {
            throw new IllegalArgumentException("Idade maiores que 20 são invalidas");
        }
    }

    private void validarPeso(Pet pet) {
        String peso = pet.getPeso();

        if (peso == null || peso.isBlank()) {
            pet.setPeso(NAO_INFORMADO);
            return;
        }

        if (!peso.matches("[0-9.,]+$")) {
            throw new IllegalArgumentException("Sómente números são validos");
        }

        double pesoRecebido = Double.parseDouble(peso.replace(",","."));
        if (pesoRecebido < 0.5 || pesoRecebido > 60) {
            throw new IllegalArgumentException("O Peso deve estár no intervalo de 0.5 a 60 kg");
        }
    }

    private void validarRaca(Pet pet){
        String raca = pet.getRaca();

        if (raca == null || raca.isBlank()) {
            pet.setRaca(NAO_INFORMADO);
            return;
        }

        if (!raca.matches("[A-Za-z]+$")) {
            throw new IllegalArgumentException("Caracteres especiais e números não são permitidos");
        }
    }

}
