package com.pawsmanager.api.pet;

import com.pawsmanager.api.exceptions.ResourceNotFoundException;
import com.pawsmanager.api.tutor.Tutor;
import com.pawsmanager.api.tutor.TutorRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetService {

    private final PetRepository petRepository;
    private final TutorRepository tutorRepository;

    public PetService(PetRepository petRepository, TutorRepository tutorRepository) {
        this.petRepository = petRepository;
        this.tutorRepository = tutorRepository;
    }

    @Cacheable(value = "petsList", key = "'all'")
    public List<PetResponseDto> listarPets() {
        return petRepository.findAll().stream()
                .map(this::toResponseDto)
                .toList();
    }


    @CacheEvict(value = "petsList", allEntries = true)
    public PetResponseDto registrarPet(PetCreateDto petCreate) {
        if (petCreate.tutorId() == null) {
            throw new ResourceNotFoundException("Tutor deve ser informado");
        }

        Tutor tutor = tutorRepository.findById(petCreate.tutorId())
                .orElseThrow(() -> new ResourceNotFoundException("Tutor não encontrado"));

        Pet pet = new Pet();
        pet.setNomePet(petCreate.nomePet());
        pet.setPetTipo(petCreate.petType());
        pet.setPetSexo(petCreate.petGender());
        pet.setIdade(petCreate.idade());
        pet.setPeso(petCreate.peso());
        pet.setRaca(petCreate.raca());
        pet.setTutor(tutor);

        pet = petRepository.save(pet);
        return toResponseDto(pet);
    }

    @Caching(evict = {
            @CacheEvict(value = "pets", key = "#id"),
            @CacheEvict(value = "petsList", allEntries = true)
    })
    public PetResponseDto updatePet(Long id, PetUpdateDto petDetails) {

        Pet existingPet = petRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Pet com o ID: " + id + " não encontrado"));

        if (petDetails.nomePet() != null) {
            existingPet.setNomePet(petDetails.nomePet());
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

        existingPet = petRepository.save(existingPet);
        return toResponseDto(existingPet);
    }

    @Cacheable(value = "petsList", key = "'nome:' + #nome")
    public List<PetResponseDto> buscarPorNome(String nome) {
        List<Pet> listaRetorno = petRepository.findByNomePetContainingIgnoreCase(nome);
        return listaRetorno.stream().map(this::toResponseDto).toList();
    }

    @Cacheable(value = "petsList", key = "'sexo:' + #sexo")
    public List<PetResponseDto> buscarPorSexo(String sexo) {
        List<Pet> listaRetorno = petRepository.findByPetGender(PetGender.valueOf(sexo.toUpperCase()));
        return listaRetorno.stream().map(this::toResponseDto).toList();
    }

    @Cacheable(value = "petsList", key = "'idade:' + #idade")
    public List<PetResponseDto> buscarPorIdade(Integer idade) {
        List<Pet> listarRetorno = petRepository.findByIdade(idade);
        return listarRetorno.stream().map(this::toResponseDto).toList();
    }

    @Cacheable(value = "pets", key = "#id")
    public PetResponseDto buscarPorId(Long id) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pet com o ID: " + id + " não encontrado"));
        return toResponseDto(pet);
    }

    @Cacheable(value = "petsList", key = "'tutor:' + #tutorId")
    public List<PetResponseDto> buscarPetsPorTutorId(Long tutorId) {

        if (!tutorRepository.existsById(tutorId)) {
            throw new ResourceNotFoundException("Tutor com o ID: " + tutorId + " não encontrado");
        }

        List<Pet> pets = petRepository.findByTutorId(tutorId);

        return pets.stream().map(this::toResponseDto).toList();
    }

    @Caching(evict = {
            @CacheEvict(value = "pets", key = "#id"),
            @CacheEvict(value = "petsList", allEntries = true)
    }
    )
    public void deletarPet(Long id) {

        if (!petRepository.existsById(id)) {
            throw new ResourceNotFoundException("Pet com o id: " + id + " não encontrado!");
        }
        petRepository.deleteById(id);
    }

    private PetResponseDto toResponseDto(Pet pet) {
        return new PetResponseDto(
                pet.getId(),
                pet.getNomePet(),
                pet.getPetTipo(),
                pet.getPetSexo(),
                pet.getIdade(),
                pet.getPeso(),
                pet.getRaca(),
                pet.getTutor() != null ? pet.getTutor().getId() : null
        );
    }
}
