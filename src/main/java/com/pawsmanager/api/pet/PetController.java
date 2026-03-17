package com.pawsmanager.api.pet;

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
        PetResponseDto response = petService.registrarPet(petdto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PetResponseDto> updatePet(@PathVariable Long id, @Valid @RequestBody PetUpdateDto petDetails) {
        PetResponseDto responseDto = petService.updatePet(id, petDetails);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/buscar/nome")
    public ResponseEntity<List<PetResponseDto>> buscarPetPorNome(@RequestParam(required = false) String nome) {
        if (nome == null || nome.isBlank()) {
            return listarPets();
        }
        List<PetResponseDto> petList = petService.buscarPorNome(nome);
        return ResponseEntity.ok(petList);
    }

    @GetMapping("/buscar/sexo")
    public ResponseEntity<List<PetResponseDto>> buscarPorSexo(@RequestParam(required = false) String sexo) {
        if (sexo == null || sexo.isBlank()) {
            return listarPets();
        }
        List<PetResponseDto> petList = petService.buscarPorSexo(sexo);
        return ResponseEntity.ok(petList);
    }

    @GetMapping("/buscar/idade")
    public ResponseEntity<List<PetResponseDto>> buscarPorIdade(@RequestParam(required = false) Integer idade) {
        if (idade == null) {
            return listarPets();
        }
        List<PetResponseDto> petList = petService.buscarPorIdade(idade);
        return ResponseEntity.ok(petList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PetResponseDto> buscarPorId(@PathVariable Long id) {
        PetResponseDto responseDto = petService.buscarPorId(id);
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
