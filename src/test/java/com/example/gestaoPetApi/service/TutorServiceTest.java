package com.example.gestaoPetApi.service;

import com.example.gestaoPetApi.dto.TutorCreateDto;
import com.example.gestaoPetApi.dto.TutorResponseDto;
import com.example.gestaoPetApi.dto.TutorUpdateDto;
import com.example.gestaoPetApi.exceptions.RecursoNaoEncontradoException;
import com.example.gestaoPetApi.model.*;
import com.example.gestaoPetApi.repository.TutorRepository;

import static org.mockito.Mockito.*;

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

@ExtendWith(MockitoExtension.class)
public class TutorServiceTest {

    @InjectMocks
    private TutorService tutorService;

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

    private EnderecoTutor criarEderecoPadrao() {
        EnderecoTutor enderecoTutor = new EnderecoTutor();
        enderecoTutor.setRua("Rua das Acácias");
        enderecoTutor.setCidade("Belo Horizonte");
        enderecoTutor.setNumeroCasa("55");

        return enderecoTutor;
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


    private TutorCreateDto criarTutorCreateDto() {
        EnderecoTutor endereco = new EnderecoTutor();
        endereco.setRua("Rua das Acácias");
        endereco.setCidade("Belo Horizonte");
        endereco.setNumeroCasa("55");

        return new TutorCreateDto(
                "João Silva",
                "joao@mail.com",
                "1234567891",
                endereco
        );
    }

    @Test
    @DisplayName("Deve retornar uma lista com um tutor")
    void deveRetornarUmalistaComUmTutor() {

        when(tutorRepository.findAll()).thenReturn(Collections.singletonList(tutorPadrao));

        List<TutorResponseDto> tutores = tutorService.listarTutores();

        Assertions.assertEquals(1, tutores.size());
        verify(tutorRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve retornar uma lista de tutores vazia")
    void deveRetornarListaVazia() {
        when(tutorRepository.findAll()).thenReturn(List.of());

        List<TutorResponseDto> resultado = tutorService.listarTutores();

        Assertions.assertEquals(0, resultado.size());
        verify(tutorRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve cadastrar um tutor com sucesso")
    void deveCadastrarUmTutorComSucesso() {

        TutorCreateDto tutorCreateDto = criarTutorCreateDto();

        when(tutorRepository.save(any(Tutor.class))).thenReturn(tutorPadrao);

        TutorResponseDto resultado = tutorService.registrarTutor(tutorCreateDto);

        Assertions.assertEquals(1L, resultado.id());
        Assertions.assertEquals(tutorCreateDto.nome(), resultado.nome());
        Assertions.assertEquals(tutorCreateDto.email(), resultado.email());
        Assertions.assertEquals(tutorCreateDto.telefone(), resultado.telefone());


        verify(tutorRepository).save(any(Tutor.class));
    }

    @Test
    @DisplayName("Deve deletar tutor")
    void deveDeletarTutor() {
        when(tutorRepository.existsById(TUTOR_ID)).thenReturn(true);

        tutorService.deletarTutor(TUTOR_ID);

        verify(tutorRepository).existsById(TUTOR_ID);
        verify(tutorRepository).deleteById(TUTOR_ID);
    }

    @Test
    @DisplayName("Deve lançar exceção se o tutor não for encontrado")
    void deveLancarExcecaoTutorNaoEcontrado() {

        TutorUpdateDto tutorUpdateDto = new TutorUpdateDto(
                "João Silva",
                "joao@mail.com",
                "(99) 00111-2212",
                criarEderecoPadrao());

        when(tutorRepository.findById(TUTOR_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(RecursoNaoEncontradoException.class,
                () -> tutorService.updateTutor(TUTOR_ID, tutorUpdateDto));

        verify(tutorRepository).findById(TUTOR_ID);
        verify(tutorRepository, never()).save(any(Tutor.class));
    }

    @Test
    @DisplayName("Atualiza o nome corretamente")
    void atualizaNome() {
        String novoNome = "Novo nome";

        TutorUpdateDto tutorUpdateDto = new TutorUpdateDto(
                novoNome,
                "joao@mail.com",
                "(99) 99111-2212",
                criarEderecoPadrao());

        when(tutorRepository.findById(1L)).thenReturn(Optional.of(tutorPadrao));
        when(tutorRepository.save(any(Tutor.class))).thenReturn(tutorPadrao);

        TutorResponseDto atualizado = tutorService.updateTutor(TUTOR_ID, tutorUpdateDto);

        Assertions.assertEquals(tutorUpdateDto.nome(), atualizado.nome());

        verify(tutorRepository).findById(TUTOR_ID);
        verify(tutorRepository).save(any(Tutor.class));
    }

    @Test
    @DisplayName("Atualiza o email corretamente")
    void atualizaEmail() {
        String novoEmail = "novoemail@mail.com";
        TutorUpdateDto tutorUpdateDto = new TutorUpdateDto("João Silva",
                novoEmail,
                "(99) 99111-2212",
                criarEderecoPadrao());

        when(tutorRepository.findById(1L)).thenReturn(Optional.of(tutorPadrao));
        when(tutorRepository.save(any(Tutor.class))).thenReturn(tutorPadrao);

        TutorResponseDto atualizado = tutorService.updateTutor(TUTOR_ID, tutorUpdateDto);

        Assertions.assertEquals(tutorUpdateDto.email(), atualizado.email());

        verify(tutorRepository).findById(TUTOR_ID);
        verify(tutorRepository).save(any(Tutor.class));
    }

    @Test
    @DisplayName("Atualiza o telefone corretamente")
    void atualizaTelefone() {
        String novoTelefone = "(99) 91234-5678";


        TutorUpdateDto tutorUpdateDto = new TutorUpdateDto("João Silva", "joao@mail.com", novoTelefone,criarEderecoPadrao());

        when(tutorRepository.findById(1L)).thenReturn(Optional.of(tutorPadrao));
        when(tutorRepository.save(any(Tutor.class))).thenReturn(tutorPadrao);

        TutorResponseDto atualizado = tutorService.updateTutor(1L, tutorUpdateDto);

        Assertions.assertEquals(tutorUpdateDto.telefone(), atualizado.telefone());

        verify(tutorRepository).findById(TUTOR_ID);
        verify(tutorRepository).save(any(Tutor.class));
    }
}
