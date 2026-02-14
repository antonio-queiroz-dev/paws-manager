package com.example.gestaoPetApi.service;

import com.example.gestaoPetApi.dto.PetCreateDto;
import com.example.gestaoPetApi.dto.PetResponseDto;
import com.example.gestaoPetApi.dto.PetUpdateDto;
import com.example.gestaoPetApi.exceptions.RecursoNaoEcontradoException;
import com.example.gestaoPetApi.model.Pet;
import com.example.gestaoPetApi.model.PetSexo;
import com.example.gestaoPetApi.model.Tutor;
import com.example.gestaoPetApi.repository.PetRepository;
import com.example.gestaoPetApi.repository.TutorRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PetService {

    private final PetRepository petRepository;
    private final TutorRepository tutorRepository;

    public PetService(PetRepository petRepository, TutorRepository tutorRepository) {
        this.petRepository = petRepository;
        this.tutorRepository = tutorRepository;
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
        if (petCreate.tutorId() == null) {
            throw new RecursoNaoEcontradoException("Tutor deve ser informado");
        }

        Tutor tutor = tutorRepository.findById(petCreate.tutorId())
                .orElseThrow(() -> new RecursoNaoEcontradoException("Tutor não encontrado"));

        Pet pet = new Pet();
        pet.setNomePet(petCreate.nomePet());
        pet.setPetTipo(petCreate.petTipo());
        pet.setPetSexo(petCreate.petSexo());
        pet.setPetEndereco(petCreate.petEndereco());
        pet.setIdade(petCreate.idade());
        pet.setPeso(petCreate.peso());
        pet.setRaca(petCreate.raca());
        pet.setTutor(tutor);

        return petRepository.save(pet);
    }

    @CacheEvict(value = "pets", allEntries = true)
    public Pet updatePet(Long id, PetUpdateDto petDetails) {

        Pet existingPet = petRepository.findById(id).
                orElseThrow(() -> new RecursoNaoEcontradoException("Pet com o ID: " + id + " não encontrado"));

        if (petDetails.nomePet() != null) {
            existingPet.setNomePet(petDetails.nomePet());
        }
        if (petDetails.petEndereco() != null) {
            existingPet.setPetEndereco(petDetails.petEndereco());
        }
        if (petDetails.idade() != null) {
            existingPet.setIdade(petDetails.idade());
        }
        if (petDetails.peso() != null) {
            existingPet.setPeso(petDetails.peso());
        }
        if (petDetails.raca() != null) {
            existingPet.setRaca(petDetails.raca());
        }

        return petRepository.save(existingPet);
    }

    public List<PetResponseDto> buscarPorNome(String nome) {
        List<Pet> listaRetorno;

        listaRetorno = petRepository.findByNomePetContainingIgnoreCase(nome);
        return listaRetorno.stream().map(this::toResponseDto).toList();
    }

    public List<PetResponseDto> buscarPorSexo(String sexo) {
        List<Pet> listaRetorno;

        listaRetorno = petRepository.findByPetSexo(PetSexo.valueOf(sexo.toUpperCase()));
        return listaRetorno.stream().map(this::toResponseDto).toList();
    }

    public List<PetResponseDto> buscarPorIdade(Integer idade) {
        List<Pet> listarRetorno;

        listarRetorno = petRepository.findByIdade(idade);

        return listarRetorno.stream().map(this::toResponseDto).toList();
    }

    public Pet buscarPorId(Long id) {
        return petRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEcontradoException("Pet com o ID: " + id + " não encontrado"));
    }

    public List<PetResponseDto> buscarPetsPorTutorId(Long tutorId) {

        if (!tutorRepository.existsById(tutorId)) {
            throw new RecursoNaoEcontradoException("Tutor com o ID: " + tutorId + " não encontrado");
        }

        List<Pet> pets = petRepository.findByTutorId(tutorId);

        List<PetResponseDto> resultado = new ArrayList<>();

        for (Pet pet : pets) {
            PetResponseDto petResponseDto = toResponseDto(pet);
            resultado.add(petResponseDto);
        }

        return resultado;

    }

    @CacheEvict(value = "pets", allEntries = true)
    public void deletarPet(Long id) {

        if (!petRepository.existsById(id)) {
            throw new RecursoNaoEcontradoException("Pet com o id: " + id + " não encontrado!");
        }
        petRepository.deleteById(id);
    }
}
