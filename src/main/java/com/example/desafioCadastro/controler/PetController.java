package com.example.desafioCadastro.controler;

import com.example.desafioCadastro.dto.PetCreateDto;
import com.example.desafioCadastro.dto.PetResponseDto;
import com.example.desafioCadastro.dto.PetUpdateDto;
import com.example.desafioCadastro.model.Pet;
import com.example.desafioCadastro.service.PetService;
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
    public ResponseEntity<List<Pet>> listarPets() {
        List<Pet> pets = petService.listarPets();
        return ResponseEntity.ok(pets);
    }

    @PostMapping
    public ResponseEntity<PetResponseDto> cadastrarPet(@RequestBody PetCreateDto petdto) {
        Pet novoPet = petService.registrarPet(petdto);

        PetResponseDto response = new PetResponseDto(
                novoPet.getId(),
                novoPet.getNomePet(),
                novoPet.getPetTipo(),
                novoPet.getPetSexo(),
                novoPet.getPetEndereco(),
                novoPet.getIdade(),
                novoPet.getPeso(),
                novoPet.getRaca()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pet> updatePet(@PathVariable Long id, @RequestBody PetUpdateDto petDetails) {
        Pet updatePet = petService.updatePet(id, petDetails);
        return ResponseEntity.ok(updatePet);
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Pet>> buscarPet(@RequestParam String termo) {
        List<Pet> petList = petService.buscar(termo);
        return ResponseEntity.ok(petList);
    }
}
