package com.example.desafioCadastro.service;

import com.example.desafioCadastro.model.Pet;
import com.example.desafioCadastro.repository.PetRespository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetService {

    private final PetRespository petRespository;

    public PetService(PetRespository petRespository) {
        this.petRespository = petRespository;
    }

    public List<Pet> listarPets() {
        return petRespository.findAll();
    }

    public Pet registrarPet(Pet pet) {
        return petRespository.save(pet);
    }
}
