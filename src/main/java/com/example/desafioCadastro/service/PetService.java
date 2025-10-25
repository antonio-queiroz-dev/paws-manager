package com.example.desafioCadastro.service;

import com.example.desafioCadastro.model.Pet;
import com.example.desafioCadastro.repository.PetRepository;
import com.example.desafioCadastro.utils.PetValidator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PetService {

    private final PetRepository petRepository;
    private final PetValidator petValidator;

    public PetService(PetRepository petRepository, PetValidator petValidator) {
        this.petRepository = petRepository;
        this.petValidator = petValidator;
    }

    public List<Pet> listarPets() {
        return petRepository.findAll();
    }

    public Pet registrarPet(Pet pet) {
        petValidator.validar(pet);

        return petRepository.save(pet);
    }

    public Pet updatePet(Long id, Pet petDetails) {
        Optional<Pet> optionalPet = petRepository.findById(id);

        if (optionalPet.isEmpty()) {
            throw new RuntimeException("Pet com o ID: "+ id + " não encontrado");
        }

        Pet existingPet = optionalPet.get();

        existingPet.setNomePet(petDetails.getNomePet());
        existingPet.setPetEndereco(petDetails.getPetEndereco());
        existingPet.setIdade(petDetails.getIdade());
        existingPet.setPeso(petDetails.getPeso());
        existingPet.setRaca(petDetails.getRaca());

        return petRepository.save(existingPet);
    }
}
