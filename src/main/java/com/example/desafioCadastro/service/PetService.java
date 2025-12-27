package com.example.desafioCadastro.service;

import com.example.desafioCadastro.dto.PetCreateDto;
import com.example.desafioCadastro.dto.PetResponseDto;
import com.example.desafioCadastro.dto.PetUpdateDto;
import com.example.desafioCadastro.exceptions.RecursoNaoEcontradoException;
import com.example.desafioCadastro.model.Pet;
import com.example.desafioCadastro.model.PetSexo;
import com.example.desafioCadastro.repository.PetRepository;
import com.example.desafioCadastro.utils.PetValidator;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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

    @Cacheable(value = "pets")
    public List<PetResponseDto> listarPets() {
        return petRepository.findAll().stream().map(this::toResponseDto).toList();
    }

    private PetResponseDto toResponseDto(Pet pet) {
        return new PetResponseDto(
                pet.getId(),
                pet.getNomePet(),
                pet.getPetTipo(),
                pet.getPetSexo(),
                pet.getPetEndereco(),
                pet.getIdade(),
                pet.getPeso(),
                pet.getRaca(),
                pet.getTutor() != null ? pet.getTutor().getId() : null
        );
    }

    @CacheEvict(value = "pets", allEntries = true)
    public Pet registrarPet(PetCreateDto petCreate) {
        petValidator.validarPetCreate(petCreate);

        Pet pet = new Pet();
        pet.setNomePet(petCreate.getNomePet());
        pet.setPetTipo(petCreate.getPetTipo());
        pet.setPetSexo(petCreate.getPetSexo());
        pet.setPetEndereco(petCreate.getPetEndereco());
        pet.setIdade(petCreate.getIdade());
        pet.setPeso(petCreate.getPeso());
        pet.setRaca(petCreate.getRaca());

        return petRepository.save(pet);
    }

    @CacheEvict(value = "pets", allEntries = true)
    public Pet updatePet(Long id, PetUpdateDto petDetails) {
        Optional<Pet> optionalPet = petRepository.findById(id);

        if (optionalPet.isEmpty()) {
            throw new RecursoNaoEcontradoException("Pet com o ID: " + id + " não encontrado");
        }

        Pet existingPet = optionalPet.get();

        if (petDetails.nomePet() != null) {
            petValidator.validarNome(petDetails.nomePet());
            existingPet.setNomePet(petDetails.nomePet());
        }
        if (petDetails.petEndereco() != null) {
            existingPet.setPetEndereco(petDetails.petEndereco());
        }
        if (petDetails.idade() != null) {
            petValidator.validarIdade(petDetails.idade());
            existingPet.setIdade(petDetails.idade());
        }
        if (petDetails.peso() != null) {
            petValidator.validarPeso(petDetails.peso());
            existingPet.setPeso(petDetails.peso());
        }
        if (petDetails.raca() != null) {
            petValidator.validarRaca(petDetails.raca());
            existingPet.setRaca(petDetails.raca());
        }

        return petRepository.save(existingPet);
    }

    public List<PetResponseDto> buscar(String termo) {
        List<Pet> listaRetorno;

        if (termo.equalsIgnoreCase("macho") || termo.equalsIgnoreCase("femea")) {
            listaRetorno = petRepository.findByPetSexo(PetSexo.valueOf(termo.toUpperCase()));
        }

        if (termo.matches("\\d+")) {
            listaRetorno = petRepository.findByIdadeContainingIgnoreCase(termo);
        }
        listaRetorno = petRepository.findByNomePetContainingIgnoreCase(termo);

        return listaRetorno.stream().map(this::toResponseDto).toList();
    }

    @CacheEvict(value = "pets", allEntries = true)
    public void deletarPet(Long id) {

        if (!petRepository.existsById(id)) {
            throw new RecursoNaoEcontradoException("Pet com o id: " + id + " não encontrado!");
        }
        petRepository.deleteById(id);
    }
}
