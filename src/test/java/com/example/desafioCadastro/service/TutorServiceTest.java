package com.example.desafioCadastro.service;

import com.example.desafioCadastro.dto.TutorCreateDto;
import com.example.desafioCadastro.dto.TutorResponseDto;
import com.example.desafioCadastro.dto.TutorUpdateDto;
import com.example.desafioCadastro.exceptions.RecursoNaoEcontradoException;
import com.example.desafioCadastro.model.Pet;
import com.example.desafioCadastro.model.Tutor;
import com.example.desafioCadastro.repository.TutorRepository;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @Test
    @DisplayName("Deve retornar uma lista com um tutor")
    void deveRetornarUmalistaComUmTutor() {
        List<Pet> pets = new ArrayList<>();
        Tutor tutor = new Tutor(1L, "João Silva", "joao@mail.com", "(99) 99111-2212", pets);

        when(tutorRepository.findAll()).thenReturn(Collections.singletonList(tutor));
        List<TutorResponseDto> tutores = tutorService.listarTutores();

        Assertions.assertEquals(1, tutores.size());
    }

    @Test
    @DisplayName("Deve retornar uma lista de tutores vazia")
    void deveRetornarListaVazia() {
        when(tutorRepository.findAll()).thenReturn(List.of());

        List<TutorResponseDto> tutores = tutorService.listarTutores();

        Assertions.assertEquals(0, tutores.size());
    }

    @Test
    @DisplayName("Deve cadastrar um tutor com sucesso")
    void deveCadastrarUmTutorComSucesso() {
        List<Pet> pets = new ArrayList<>();

        TutorCreateDto tutorCreateDto = new TutorCreateDto("João Silva", "joao@mail.com", "(99) 99111-2212");
        Tutor tutorSalvo = new Tutor(1L, "João Silva", "joao@mail.com", "(99) 99111-2212", pets);

        when(tutorRepository.save(any(Tutor.class))).thenReturn(tutorSalvo);

        Tutor resultado = tutorService.registrarTutor(tutorCreateDto);

        Assertions.assertEquals(1L,resultado.getId());
        Assertions.assertEquals(tutorCreateDto.nome(),resultado.getNome());
        Assertions.assertEquals(tutorCreateDto.email(),resultado.getEmail());
        Assertions.assertEquals(tutorCreateDto.telefone(),resultado.getTelefone());

    }

    @Test
    @DisplayName("Deve deletar tutor")
    void deveDeletarTutor() {
        when(tutorRepository.existsById(1L)).thenReturn(true);

        tutorService.deletarTutor(1L);

        verify(tutorRepository).deleteById(1L);

    }

    @Test
    @DisplayName("Deve lançar exceção se o tutor não for encontrado")
    void deveLancarExcecaoTutorNaoEcontrado() {
        when(tutorRepository.findById(1L)).thenReturn(Optional.empty());

        TutorUpdateDto tutorUpdateDto = new TutorUpdateDto("João Silva","joao@mail.com","(99) 00111-2212");

        Assertions.assertThrows(RecursoNaoEcontradoException.class, () -> tutorService.updateTutor(1L,tutorUpdateDto));

    }

    @Test
    @DisplayName("Atualiza o nome corretamente")
    void atualizaNome() {
        List<Pet> pets = new ArrayList<>();
        Tutor tutorExistente = new Tutor(1L, "Antigo nome", "joao@mail.com", "(99) 99111-2212", pets);

        TutorUpdateDto tutorUpdateDto = new TutorUpdateDto("Novo nome", "joao@mail.com", "(99) 99111-2212");

        when(tutorRepository.findById(1L)).thenReturn(Optional.of(tutorExistente));
        when(tutorRepository.save(tutorExistente)).thenReturn(tutorExistente);

        Tutor atualizado = tutorService.updateTutor(1L,tutorUpdateDto);

        Assertions.assertEquals(tutorUpdateDto.nome(),atualizado.getNome());
    }

    @Test
    @DisplayName("Atualiza o email corretamente")
    void atualizaEmail() {
        List<Pet> pets = new ArrayList<>();
        Tutor tutorExistente = new Tutor(1L, "João Silva", "antigoemail@mail.com", "(99) 99111-2212", pets);

        TutorUpdateDto tutorUpdateDto = new TutorUpdateDto("João Silva", "novoemail@mail.com", "(99) 99111-2212");

        when(tutorRepository.findById(1L)).thenReturn(Optional.of(tutorExistente));
        when(tutorRepository.save(tutorExistente)).thenReturn(tutorExistente);

        Tutor atualizado = tutorService.updateTutor(1L,tutorUpdateDto);

        Assertions.assertEquals(tutorUpdateDto.email(),atualizado.getEmail());
    }

    @Test
    @DisplayName("Atualiza o telefone corretamente")
    void atualizaTelefone() {
        List<Pet> pets = new ArrayList<>();
        Tutor tutorExistente = new Tutor(1L, "João Silva", "joao@mail.com", "(99) 99111-2212", pets);

        TutorUpdateDto tutorUpdateDto = new TutorUpdateDto("João Silva", "joao@mail.com", "(99) 91234-5678");

        when(tutorRepository.findById(1L)).thenReturn(Optional.of(tutorExistente));
        when(tutorRepository.save(tutorExistente)).thenReturn(tutorExistente);

        Tutor atualizado = tutorService.updateTutor(1L,tutorUpdateDto);

        Assertions.assertEquals(tutorUpdateDto.telefone(),atualizado.getTelefone());
    }
}
