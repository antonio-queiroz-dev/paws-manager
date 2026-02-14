package com.example.gestaoPetApi.controller;

import com.example.gestaoPetApi.dto.PetCreateDto;
import com.example.gestaoPetApi.dto.PetResponseDto;
import com.example.gestaoPetApi.dto.PetUpdateDto;
import com.example.gestaoPetApi.model.Pet;
import com.example.gestaoPetApi.service.PetService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/pets")
public class PetController {

    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @GetMapping
    public ResponseEntity<List<PetResponseDto>> listarPets() {
        List<PetResponseDto> pets = petService.listarPets();
        return ResponseEntity.ok(pets);
    }

    @PostMapping
    public ResponseEntity<PetResponseDto> cadastrarPet(@Valid @RequestBody PetCreateDto petdto) {
        Pet novoPet = petService.registrarPet(petdto);

        PetResponseDto response = new PetResponseDto(
                novoPet.getId(),
                novoPet.getNomePet(),
                novoPet.getPetTipo(),
                novoPet.getPetSexo(),
                novoPet.getPetEndereco(),
                novoPet.getIdade(),
                novoPet.getPeso(),
                novoPet.getRaca(),
                petdto.tutorId()
                );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PetResponseDto> updatePet(@PathVariable Long id, @RequestBody PetUpdateDto petDetails) {
        Pet petAtualizado = petService.updatePet(id, petDetails);

        PetResponseDto responseDto = new PetResponseDto(
                petAtualizado.getId(),
                petAtualizado.getNomePet(),
                petAtualizado.getPetTipo(),
                petAtualizado.getPetSexo(),
                petAtualizado.getPetEndereco(),
                petAtualizado.getIdade(),
                petAtualizado.getPeso(),
                petAtualizado.getRaca(),
                petAtualizado.getTutor() != null ? petAtualizado.getTutor().getId() : null
        );

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/buscar/nome")
    public ResponseEntity<List<PetResponseDto>> buscarPetPorNome(@RequestParam String nome) {
        List<PetResponseDto> petList = petService.buscarPorNome(nome);
        return ResponseEntity.ok(petList);
    }

    @GetMapping("/buscar/sexo")
    public ResponseEntity<List<PetResponseDto>> buscarPorSexo(@RequestParam String sexo) {
        List<PetResponseDto> petList = petService.buscarPorSexo(sexo);
        return ResponseEntity.ok(petList);
    }

    @GetMapping("/buscar/idade")
    public ResponseEntity<List<PetResponseDto>> buscarPorIdade(@RequestParam Integer idade) {
        List<PetResponseDto> petList = petService.buscarPorIdade(idade);
        return ResponseEntity.ok(petList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PetResponseDto> buscarPorId(@PathVariable Long id) {
        Pet pet = petService.buscarPorId(id);

        PetResponseDto responseDto = new PetResponseDto(
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
        return ResponseEntity.ok(responseDto);

    }

    @GetMapping("/tutor/{tutorId}")
    public ResponseEntity<List<PetResponseDto>> buscarPetsPorTutorId(@PathVariable Long tutorId) {
        List<PetResponseDto> petList = petService.buscarPetsPorTutorId(tutorId);
        return ResponseEntity.ok(petList);
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPet(@PathVariable Long id) {
        petService.deletarPet(id);
        return ResponseEntity.noContent().build();
    }
}
