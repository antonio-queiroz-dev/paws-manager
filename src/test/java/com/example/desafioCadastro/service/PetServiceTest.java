package com.example.desafioCadastro.service;

import com.example.desafioCadastro.dto.PetCreateDto;
import com.example.desafioCadastro.dto.PetResponseDto;
import com.example.desafioCadastro.dto.PetUpdateDto;
import com.example.desafioCadastro.exceptions.RecursoNaoEcontradoException;
import com.example.desafioCadastro.model.*;
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
import java.util.Optional;

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
        Tutor tutor = new Tutor();
        tutor.setNome("João");
        PetEndereco endereco = new PetEndereco();
        endereco.setRua("Rua das Acácias");
        endereco.setCidade("Belo Horizonte");
        endereco.setNumeroCasa("55");
        Pet pet = new Pet(1L, "José caça rato", PetTipo.GATO, PetSexo.MACHO, endereco, "5", "4", "Siames", tutor);

        when(petRepository.findAll()).thenReturn(Collections.singletonList(pet));
        List<PetResponseDto> pets = petService.listarPets();

        Assertions.assertEquals(1, pets.size());
    }

    @Test
    @DisplayName("Deve retornar uma lista de pets vazia")
    void deveRetornarListaVazia() {
        when(petRepository.findAll()).thenReturn(List.of());

        List<PetResponseDto> pets = petService.listarPets();

        Assertions.assertEquals(0,pets.size());

    }

    @Test
    @DisplayName("Deve cadastrar um pet com sucesso")
    void deveCadastrarUmPetComSucesso() {
        Tutor tutor = new Tutor();
        tutor.setNome("João");

        PetEndereco endereco = new PetEndereco();
        endereco.setRua("Rua das Acácias");
        endereco.setCidade("Belo Horizonte");
        endereco.setNumeroCasa("55");
        PetCreateDto petCreateDto = new PetCreateDto("José caça rato", PetTipo.GATO, PetSexo.MACHO, endereco, "5", "4", "Siames",tutor.getId());

        Pet petSalvo = new Pet(1L, "José caça rato", PetTipo.GATO, PetSexo.MACHO, endereco, "5", "4", "Siames",tutor);

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

    @Test
    @DisplayName("Deve lançar exceção se o pet não for encontrado")
    void deveLancarExcecaoPetNaoEcontrado() {
        PetEndereco endereco = new PetEndereco();
        endereco.setRua("Rua das Acácias");
        endereco.setCidade("Belo Horizonte");
        endereco.setNumeroCasa("55");

        when(petRepository.findById(1L)).thenReturn(Optional.empty());
        PetUpdateDto petUpdateDto = new PetUpdateDto("José caça rato", endereco, "5", "4", "Siames");

        Assertions.assertThrows(RecursoNaoEcontradoException.class, () -> petService.updatePet(1L,petUpdateDto ));
    }

    @Test
    @DisplayName("Atualiza o nome corretamente")
    void atualizaNome() {
        Tutor tutor = new Tutor();
        tutor.setNome("João");

        PetEndereco endereco = new PetEndereco();
        endereco.setRua("Rua das Acácias");
        endereco.setCidade("Belo Horizonte");
        endereco.setNumeroCasa("55");
        Pet existente = new Pet(1L,"Antigo Nome", PetTipo.GATO, PetSexo.MACHO, endereco, "5", "4", "Siames",tutor);

        PetUpdateDto petUpdateDto = new PetUpdateDto("Novo nome", endereco, "5", "4", "Siames");

        when(petRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(petRepository.save(existente)).thenReturn(existente);

        Pet atualizado = petService.updatePet(1L, petUpdateDto);

        Assertions.assertEquals(petUpdateDto.nomePet(), atualizado.getNomePet());
    }

    @Test
    @DisplayName("Atualiza o endereco corretamente")
    void atualizaEndereco() {
        Tutor tutor = new Tutor();
        tutor.setNome("João");

        PetEndereco enderecoExistente = new PetEndereco();
        enderecoExistente.setRua("Rua das Acácias");
        enderecoExistente.setCidade("Belo Horizonte");
        enderecoExistente.setNumeroCasa("55");
        Pet existente = new Pet(1L,"José caça rato", PetTipo.GATO, PetSexo.MACHO, enderecoExistente, "5", "4", "Siames",tutor);

        PetEndereco enderecoNovo = new PetEndereco();
        enderecoNovo.setRua("Rua das Acácias");
        enderecoNovo.setCidade("Belo Horizonte");
        enderecoNovo.setNumeroCasa("55");
        PetUpdateDto petUpdateDto = new PetUpdateDto("José caça rato", enderecoNovo, "5", "4", "Siames");

        when(petRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(petRepository.save(existente)).thenReturn(existente);

        Pet atualizado = petService.updatePet(1L, petUpdateDto);

        Assertions.assertEquals(petUpdateDto.petEndereco(), atualizado.getPetEndereco());
    }

    @Test
    @DisplayName("Atualiza a idade corretamente")
    void atualizaIdade() {
        Tutor tutor = new Tutor();
        tutor.setNome("João");

        PetEndereco endereco = new PetEndereco();
        endereco.setRua("Rua das Acácias");
        endereco.setCidade("Belo Horizonte");
        endereco.setNumeroCasa("55");
        Pet existente = new Pet(1L,"José caça rato", PetTipo.GATO, PetSexo.MACHO, endereco, "5", "4", "Siames", tutor);

        PetUpdateDto petUpdateDto = new PetUpdateDto("José caça rato", endereco, "10", "4", "Siames");

        when(petRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(petRepository.save(existente)).thenReturn(existente);

        Pet atualizado = petService.updatePet(1L, petUpdateDto);

        Assertions.assertEquals(petUpdateDto.idade(), atualizado.getIdade());
    }

    @Test
    @DisplayName("Atualiza peso corretamente")
    void atualizaPeso() {
        Tutor tutor = new Tutor();
        tutor.setNome("João");

        PetEndereco endereco = new PetEndereco();
        endereco.setRua("Rua das Acácias");
        endereco.setCidade("Belo Horizonte");
        endereco.setNumeroCasa("55");
        Pet existente = new Pet(1L,"José caça rato", PetTipo.GATO, PetSexo.MACHO, endereco, "5", "4", "Siames", tutor);

        PetUpdateDto petUpdateDto = new PetUpdateDto("José caça rato", endereco, "5", "10", "Siames");

        when(petRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(petRepository.save(existente)).thenReturn(existente);

        Pet atualizado = petService.updatePet(1L, petUpdateDto);

        Assertions.assertEquals(petUpdateDto.peso(), atualizado.getPeso());
    }

    @Test
    @DisplayName("Atualiza a raca corretamente")
    void atualizaRaca() {
        Tutor tutor = new Tutor();
        tutor.setNome("João");

        PetEndereco endereco = new PetEndereco();
        endereco.setRua("Rua das Acácias");
        endereco.setCidade("Belo Horizonte");
        endereco.setNumeroCasa("55");
        Pet existente = new Pet(1L,"José caça rato", PetTipo.GATO, PetSexo.MACHO, endereco, "5", "4", "Siames", tutor);

        PetUpdateDto petUpdateDto = new PetUpdateDto("José caça rato", endereco, "5", "4", "Persa");

        when(petRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(petRepository.save(existente)).thenReturn(existente);

        Pet atualizado = petService.updatePet(1L, petUpdateDto);

        Assertions.assertEquals(petUpdateDto.raca(), atualizado.getRaca());
    }

}