package com.example.gestaoPetApi.service;

import com.example.gestaoPetApi.dto.PetCreateDto;
import com.example.gestaoPetApi.dto.PetResponseDto;
import com.example.gestaoPetApi.dto.PetUpdateDto;
import com.example.gestaoPetApi.exceptions.RecursoNaoEncontradoException;
import com.example.gestaoPetApi.model.*;
import com.example.gestaoPetApi.repository.PetRepository;
import com.example.gestaoPetApi.repository.TutorRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
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
    private TutorRepository tutorRepository;

    private Tutor tutorPadrao;
    private Pet petPadrao;
    private static final Long PET_ID = 1L;
    private static final Long TUTOR_ID = 1L;

    @BeforeEach
    void setUp() {
        tutorPadrao = criarTutorPadrao();
        petPadrao = criarPetPadrao(tutorPadrao);
    }

    private Tutor criarTutorPadrao() {
        EnderecoTutor endereco = new EnderecoTutor();

        return new Tutor(
                TUTOR_ID,
                "João Silva",
                "joao@mail.com",
                "1234567891",
                endereco,
                new ArrayList<>()
        );
    }

    private Pet criarPetPadrao(Tutor tutor) {
        return new Pet(
                PET_ID,
                "José caça rato",
                PetTipo.GATO,
                PetSexo.MACHO,
                5,
                new BigDecimal("4.0"),
                "Siames",
                tutor
        );
    }

    private PetCreateDto criarPetCreateDto() {
        return new PetCreateDto(
                "José caça rato",
                PetTipo.GATO,
                PetSexo.MACHO,
                5,
                new BigDecimal("4.0"),
                "Siames",
                TUTOR_ID
        );
    }

    @Test
    @DisplayName("Deve retornar uma lista com um pet")
    void deveRetornarUmalistaComUmPet() {

        when(petRepository.findAll()).thenReturn(Collections.singletonList(petPadrao));

        List<PetResponseDto> resultado = petService.listarPets();

        Assertions.assertEquals(1, resultado.size());
        verify(petRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve retornar uma lista de pets vazia")
    void deveRetornarListaVazia() {
        when(petRepository.findAll()).thenReturn(List.of());

        List<PetResponseDto> resultado = petService.listarPets();

        Assertions.assertEquals(0, resultado.size());
        verify(petRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve cadastrar um pet com sucesso")
    void deveCadastrarUmPetComSucesso() {
        PetCreateDto petCreateDto = criarPetCreateDto();

        when(tutorRepository.findById(1L)).thenReturn(Optional.of(tutorPadrao));
        when(petRepository.save(any(Pet.class))).thenReturn(petPadrao);

        PetResponseDto resultado = petService.registrarPet(petCreateDto);

        Assertions.assertEquals(1L, resultado.id());
        Assertions.assertEquals(petCreateDto.nomePet(), resultado.nomePet());
        Assertions.assertEquals(petCreateDto.petTipo(), resultado.petTipo());
        Assertions.assertEquals(petCreateDto.petSexo(), resultado.petSexo());
        Assertions.assertEquals(petCreateDto.idade(), resultado.idade());
        Assertions.assertEquals(petCreateDto.peso(), resultado.peso());
        Assertions.assertEquals(petCreateDto.raca(), resultado.raca());

        verify(tutorRepository).findById(TUTOR_ID);
        verify(petRepository).save(any(Pet.class));

    }

    @Test
    @DisplayName("Deve deletar pet")
    void deveDeletarPet() {
        when(petRepository.existsById(PET_ID)).thenReturn(true);

        petService.deletarPet(PET_ID);

        verify(petRepository).existsById(PET_ID);
        verify(petRepository).deleteById(PET_ID);
    }

    @Test
    @DisplayName("Deve lançar exceção se o pet não for encontrado")
    void deveLancarExcecaoPetNaoEcontrado() {

        PetUpdateDto petUpdateDto = new PetUpdateDto("José caça rato",
                5,
                new BigDecimal("4"),
                "Siames");

        when(petRepository.findById(PET_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(RecursoNaoEncontradoException.class,
                () -> petService.updatePet(1L, petUpdateDto));

        verify(petRepository).findById(PET_ID);
        verify(petRepository, never()).save(any(Pet.class));
    }

    @Test
    @DisplayName("Atualiza o nome do pet corretamente")
    void atualizaNome() {

        String novoNome = "Novo nome";
        PetUpdateDto petUpdateDto = new PetUpdateDto(
                novoNome,
                5,
                new BigDecimal("4"),
                "Siames");

        when(petRepository.findById(PET_ID)).thenReturn(Optional.of(petPadrao));
        when(petRepository.save(any(Pet.class))).thenReturn(petPadrao);

        PetResponseDto atualizado = petService.updatePet(1L, petUpdateDto);

        Assertions.assertEquals(petUpdateDto.nomePet(), atualizado.nomePet());

        verify(petRepository).findById(PET_ID);
        verify(petRepository).save(any(Pet.class));
    }


    @Test
    @DisplayName("Atualiza a idade corretamente")
    void atualizaIdade() {
        Integer novaIdade = 10;

        PetUpdateDto petUpdateDto = new PetUpdateDto(
                "José caça rato",
                novaIdade,
                new BigDecimal("4"),
                "Siames");

        when(petRepository.findById(PET_ID)).thenReturn(Optional.of(petPadrao));
        when(petRepository.save(any(Pet.class))).thenReturn(petPadrao);

        PetResponseDto resultado = petService.updatePet(PET_ID, petUpdateDto);

        Assertions.assertEquals(petUpdateDto.idade(), resultado.idade());

        verify(petRepository).findById(PET_ID);
        verify(petRepository).save(any(Pet.class));
    }

    @Test
    @DisplayName("Atualiza peso corretamente")
    void atualizaPeso() {
        BigDecimal novoPeso = new BigDecimal("10");

        PetUpdateDto petUpdateDto = new PetUpdateDto(
                "José caça rato",
                5, novoPeso,
                "Siames");

        when(petRepository.findById(1L)).thenReturn(Optional.of(petPadrao));
        when(petRepository.save(any(Pet.class))).thenReturn(petPadrao);

        PetResponseDto atualizado = petService.updatePet(1L, petUpdateDto);

        Assertions.assertEquals(petUpdateDto.peso(), atualizado.peso());

        verify(petRepository).findById(PET_ID);
        verify(petRepository).save(any(Pet.class));
    }

    @Test
    @DisplayName("Atualiza a raca corretamente")
    void atualizaRaca() {
        Tutor tutor = new Tutor();
        tutor.setNome("João");

        Pet existente = new Pet(
                PET_ID,
                "José caça rato",
                PetTipo.GATO,
                PetSexo.MACHO,
                5,
                new BigDecimal("4"),
                "Siames", tutor);

        PetUpdateDto petUpdateDto = new PetUpdateDto("José caça rato", 5, new BigDecimal("4"), "Persa");

        when(petRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(petRepository.save(existente)).thenReturn(existente);

        PetResponseDto atualizado = petService.updatePet(1L, petUpdateDto);

        Assertions.assertEquals(petUpdateDto.raca(), atualizado.raca());

        verify(petRepository).findById(PET_ID);
        verify(petRepository).save(any(Pet.class));
    }
}