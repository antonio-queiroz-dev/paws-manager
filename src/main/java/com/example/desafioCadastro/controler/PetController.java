package com.example.desafioCadastro.controler;

import com.example.desafioCadastro.model.Pet;
import com.example.desafioCadastro.service.PetService;
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
    public List<Pet> listarPets() {
        return petService.listarPets();
    }

    @PostMapping
    public Pet cadastrarPet(@RequestBody Pet pet) {
        return petService.registrarPet(pet);
    }
}
