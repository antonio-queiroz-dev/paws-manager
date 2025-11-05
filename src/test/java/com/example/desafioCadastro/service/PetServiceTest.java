package com.example.desafioCadastro.service;

import com.example.desafioCadastro.dto.PetCreateDto;
import com.example.desafioCadastro.model.Pet;
import com.example.desafioCadastro.model.PetEndereco;
import com.example.desafioCadastro.model.PetSexo;
import com.example.desafioCadastro.model.PetTipo;
import com.example.desafioCadastro.repository.PetRepository;
import com.example.desafioCadastro.utils.PetValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PetServiceTest {

    @InjectMocks
    private PetService petService;

    @Mock
    private PetRepository petRepository;

    @Mock
    private PetValidator petValidator;

    @Test
    @DisplayName("Deve retornar uma lista com um pet")
    void deveRetornarUmalistaComUmPet() {
        PetEndereco endereco = new PetEndereco();
        endereco.setRua("Rua das Acácias");
        endereco.setCidade("Belo Horizonte");
        endereco.setNumeroCasa("55");
        Pet pet = new Pet(1L, "José caça rato", PetTipo.GATO, PetSexo.MACHO, endereco, "5", "4", "Siames");

        when(petRepository.findAll()).thenReturn(Collections.singletonList(pet));
        List<Pet> pets = petService.listarPets();

        Assertions.assertEquals(1, pets.size());
    }

    @Test
    @DisplayName("Deve retornar um lista de pets vazia")
    void deveRetornarListaVazia() {
        when(petRepository.findAll()).thenReturn(List.of());

        List<Pet> pets = petService.listarPets();

        Assertions.assertEquals(0,pets.size());

    }

    @Test
    @DisplayName("Deve cadastrar um pet com sucesso")
    void deveCadastrarUmPetComSucesso() {

        PetEndereco endereco = new PetEndereco();
        endereco.setRua("Rua das Acácias");
        endereco.setCidade("Belo Horizonte");
        endereco.setNumeroCasa("55");
        PetCreateDto petCreateDto = new PetCreateDto("José caça rato", PetTipo.GATO, PetSexo.MACHO, endereco, "5", "4", "Siames");

        Pet petSalvo = new Pet(1L, "José caça rato", PetTipo.GATO, PetSexo.MACHO, endereco, "5", "4", "Siames");

        when(petRepository.save(any(Pet.class))).thenReturn(petSalvo);

        Pet resultado = petService.registrarPet(petCreateDto);

        Assertions.assertEquals(1L, resultado.getId());
        Assertions.assertEquals(petCreateDto.getNomePet(), resultado.getNomePet());
        Assertions.assertEquals(petCreateDto.getPetTipo(), resultado.getPetTipo());
        Assertions.assertEquals(petCreateDto.getPetSexo(), resultado.getPetSexo());
        Assertions.assertEquals(petCreateDto.getPetEndereco(), resultado.getPetEndereco());
        Assertions.assertEquals(petCreateDto.getIdade(), resultado.getIdade());
        Assertions.assertEquals(petCreateDto.getPeso(), resultado.getPeso());
        Assertions.assertEquals(petCreateDto.getRaca(), resultado.getRaca());

    }

    @Test
    @DisplayName("Deve deletar pet")
    void deveDeletarPet() {
        when(petRepository.existsById(1L)).thenReturn(true);

        petService.deletarPet(1L);

        verify(petRepository).deleteById(1L);
    }


}