package com.example.desafioCadastro.service;

import com.example.desafioCadastro.dto.PetCreateDto;
import com.example.desafioCadastro.dto.PetResponseDto;
import com.example.desafioCadastro.dto.PetUpdateDto;
import com.example.desafioCadastro.exceptions.RecursoNaoEcontradoException;
import com.example.desafioCadastro.model.Pet;
import com.example.desafioCadastro.model.PetSexo;
import com.example.desafioCadastro.model.Tutor;
import com.example.desafioCadastro.repository.PetRepository;
import com.example.desafioCadastro.repository.TutorRepository;
import com.example.desafioCadastro.utils.PetValidator;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PetService {

    private final PetRepository petRepository;
    private final PetValidator petValidator;
    private final TutorRepository tutorRepository;

    public PetService(PetRepository petRepository, PetValidator petValidator, TutorRepository tutorRepository) {
        this.petRepository = petRepository;
        this.petValidator = petValidator;
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
        petValidator.validarPetCreate(petCreate);

        if (petCreate.getTutorId() == null) {
            throw new RecursoNaoEcontradoException("Tutor deve ser informado");
        }

        Tutor tutor = tutorRepository.findById(petCreate.getTutorId())
                .orElseThrow(()-> new RecursoNaoEcontradoException("Tutor não encontrado"));

        Pet pet = new Pet();
        pet.setNomePet(petCreate.getNomePet());
        pet.setPetTipo(petCreate.getPetTipo());
        pet.setPetSexo(petCreate.getPetSexo());
        pet.setPetEndereco(petCreate.getPetEndereco());
        pet.setIdade(petCreate.getIdade());
        pet.setPeso(petCreate.getPeso());
        pet.setRaca(petCreate.getRaca());
        pet.setTutor(tutor);

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

    public List<PetResponseDto> buscarPorNome(String nome) {
        List<Pet> listaRetorno;

        if (nome == null || nome.isBlank()) {
            listaRetorno = petRepository.findAll();
        }

        listaRetorno = petRepository.findByNomePetContainingIgnoreCase(nome);
        return listaRetorno.stream().map(this::toResponseDto).toList();
    }

    public List<PetResponseDto> buscarPorSexo(String sexo) {
        List<Pet> listaRetorno;

        if (sexo == null || sexo.isBlank()) {
            listaRetorno = petRepository.findAll();
        }

        listaRetorno = petRepository.findByPetSexo(PetSexo.valueOf(sexo.toUpperCase()));
        return listaRetorno.stream().map(this::toResponseDto).toList();
    }

    public List<PetResponseDto> buscarPorIdade(String idade) {
        List<Pet> listarRetorno;

        if (idade == null || idade.isBlank()) {
            listarRetorno = petRepository.findAll();
        }

        listarRetorno = petRepository.findByIdadeContainingIgnoreCase(idade);
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

        for (Pet pet: pets) {
            PetResponseDto petResponseDto = converteParaDto(pet);
            resultado.add(petResponseDto);
        }

        return resultado;

    }

    private PetResponseDto converteParaDto(Pet pet) {
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
    public void deletarPet(Long id) {

        if (!petRepository.existsById(id)) {
            throw new RecursoNaoEcontradoException("Pet com o id: " + id + " não encontrado!");
        }
        petRepository.deleteById(id);
    }
}
