package com.pawsmanager.api.tutor;

import com.pawsmanager.api.integration.BaseIntegrationTest;
import com.pawsmanager.api.tutor.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;

public class TutorIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private TutorRepository tutorRepository;

    private static final String BASE_URL = "/api/tutores";

    @BeforeEach
    void setUp() {
        tutorRepository.deleteAll();
    }

    private Tutor criarEsalvarTutor(String nome, String email, String telefone) {
        AddressTutor endereco = criarEnderecoPadrao();
        Tutor tutor = new Tutor();
        tutor.setNome(nome);
        tutor.setEmail(email);
        tutor.setTelefone(telefone);
        tutor.setEnderecoTutor(endereco);
        tutor.setPets(new java.util.ArrayList<>());
        return tutorRepository.save(tutor);
    }

    private AddressTutor criarEnderecoPadrao() {
        AddressTutor endereco = new AddressTutor();
        endereco.setRua("Rua das Acácias");
        endereco.setCidade("Belo Horizonte");
        endereco.setNumeroCasa("55");
        return endereco;
    }

    private TutorCreateDto criarTutorCreateDto(String nome, String email, String telefone) {
        return new TutorCreateDto(nome, email, telefone, criarEnderecoPadrao());
    }

    private TutorUpdateDto criarTutorUpdateDto(String nome, String email, String telefone) {
        return new TutorUpdateDto(nome, email, telefone, criarEnderecoPadrao());
    }

    @Test
    @DisplayName("Deve criar um tutor com sucesso")
    void deveCriarTutorComSucesso() {
        TutorCreateDto request = criarTutorCreateDto("João Silva", "joao@email.com", "11999999999");

        ResponseEntity<TutorResponseDto> response = testRestTemplate.exchange(
                BASE_URL,
                HttpMethod.POST,
                new HttpEntity<>(request, headersComToken()),
                TutorResponseDto.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().nome()).isEqualTo("João Silva");
        assertThat(tutorRepository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("Criar Tutor com nome inválido")
    void deveCriarTutorComNomeInvalido() {
        TutorCreateDto request = new TutorCreateDto("", "joao@mail.com", "11999999999", criarEnderecoPadrao());

        ResponseEntity<TutorResponseDto> response = testRestTemplate.exchange(
                BASE_URL,
                HttpMethod.POST,
                new HttpEntity<>(request, headersComToken()),
                TutorResponseDto.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(tutorRepository.count()).isEqualTo(0);
    }

    @Test
    @DisplayName("Criar Tutor com email inválido")
    void deveCriarTutorComEmailInvalido() {
        TutorCreateDto request = new TutorCreateDto("João Silva", "joao", "11999999999", criarEnderecoPadrao());

        ResponseEntity<TutorResponseDto> response = testRestTemplate.exchange(
                BASE_URL,
                HttpMethod.POST,
                new HttpEntity<>(request, headersComToken()),
                TutorResponseDto.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(tutorRepository.count()).isEqualTo(0);
    }

    @Test
    @DisplayName("Criar Tutor com telefone inválido")
    void deveCriarTutorComTelefoneInvalido() {
        TutorCreateDto request = criarTutorCreateDto("João Silva", "joao@mail.com", "");

        ResponseEntity<TutorResponseDto> response = testRestTemplate.exchange(
                BASE_URL,
                HttpMethod.POST,
                new HttpEntity<>(request, headersComToken()),
                TutorResponseDto.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(tutorRepository.count()).isEqualTo(0);
    }

    @Test
    @DisplayName("Deve buscar um tutor por ID existente")
    void deveBuscarTutorPorIdExistente() {
        Tutor tutorSalvo = criarEsalvarTutor("João Silva", "joao@email.com", "11999999999");

        ResponseEntity<TutorResponseDto> response = testRestTemplate.exchange(
                BASE_URL + "/" + tutorSalvo.getId(),
                HttpMethod.GET,
                new HttpEntity<>(headersComToken()),
                TutorResponseDto.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().nome()).isEqualTo("João Silva");
    }

    @Test
    @DisplayName("Deve retornar 404 ao buscar tutor inexistente")
    void deveRetornar404AoBuscarTutorInexistente() {
        ResponseEntity<TutorResponseDto> response = testRestTemplate.exchange(
                BASE_URL + "/999",
                HttpMethod.GET,
                new HttpEntity<>(headersComToken()),
                TutorResponseDto.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("Deve listar todos os tutores existentes")
    void deveListarTodosTutoresExistentes() {
        criarEsalvarTutor("João Silva", "joao@mail.com", "11999999999");
        criarEsalvarTutor("Pedro Alvares", "pedro@mail.com", "65999999999");
        criarEsalvarTutor("José Bonifácio", "jose@mail.com", "21999999999");

        ResponseEntity<TutorResponseDto[]> response = testRestTemplate.exchange(
                BASE_URL,
                HttpMethod.GET,
                new HttpEntity<>(headersComToken()),
                TutorResponseDto[].class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(3);
        assertThat(response.getBody())
                .extracting(TutorResponseDto::nome)
                .containsExactlyInAnyOrder("João Silva", "Pedro Alvares", "José Bonifácio");
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não há tutores cadastrados")
    void deveRetornarListaVazia() {
        ResponseEntity<TutorResponseDto[]> response = testRestTemplate.exchange(
                BASE_URL,
                HttpMethod.GET,
                new HttpEntity<>(headersComToken()),
                TutorResponseDto[].class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEmpty();
    }

    @Test
    @DisplayName("Atualizar um tutor existente")
    void deveAtualizarTutorExistente() {
        Tutor tutorSalvo = criarEsalvarTutor("João Silva", "joao@mail.com", "11999999999");
        TutorUpdateDto dadosAtualizados = criarTutorUpdateDto("João Atualizado", "emailAtualizado@mail.com", "1234567891");

        ResponseEntity<TutorResponseDto> response = testRestTemplate.exchange(
                BASE_URL + "/" + tutorSalvo.getId(),
                HttpMethod.PUT,
                new HttpEntity<>(dadosAtualizados, headersComToken()),
                TutorResponseDto.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().nome()).isEqualTo("João Atualizado");
        assertThat(response.getBody().email()).isEqualTo("emailAtualizado@mail.com");
        assertThat(response.getBody().telefone()).isEqualTo("1234567891");
    }

    @Test
    @DisplayName("Atualizar Tutor inexistente")
    void deveAtualizarTutorInexistente() {
        TutorUpdateDto dadosAtualizado = criarTutorUpdateDto("João Atualizado", "emailAtualizado@mail.com", "1234567891");

        ResponseEntity<TutorResponseDto> response = testRestTemplate.exchange(
                BASE_URL + "/999",
                HttpMethod.PUT,
                new HttpEntity<>(dadosAtualizado, headersComToken()),
                TutorResponseDto.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("Deletar um tutor existente")
    void deveDeletarTutorExistente() {
        Tutor tutorSalvo = criarEsalvarTutor("João Silva", "joao@email.com", "11999999999");

        ResponseEntity<Void> response = testRestTemplate.exchange(
                BASE_URL + "/" + tutorSalvo.getId(),
                HttpMethod.DELETE,
                new HttpEntity<>(headersComToken()),
                Void.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(tutorRepository.findById(tutorSalvo.getId())).isEmpty();
        assertThat(tutorRepository.count()).isEqualTo(0);
    }

    @Test
    @DisplayName("Deletar Tutor inexistente")
    void deveDeletarTutorInexistente() {
        ResponseEntity<Void> response = testRestTemplate.exchange(
                BASE_URL + "/999",
                HttpMethod.DELETE,
                new HttpEntity<>(headersComToken()),
                Void.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}