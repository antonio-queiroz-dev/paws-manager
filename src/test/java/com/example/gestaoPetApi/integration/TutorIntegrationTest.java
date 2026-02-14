package com.example.gestaoPetApi.integration;

import com.example.gestaoPetApi.dto.TutorCreateDto;
import com.example.gestaoPetApi.dto.TutorResponseDto;
import com.example.gestaoPetApi.dto.TutorUpdateDto;
import com.example.gestaoPetApi.model.Tutor;
import com.example.gestaoPetApi.repository.TutorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;


public class TutorIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TutorRepository tutorRepository;

    @BeforeEach
    void setUp() {
        tutorRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve criar um tutor com sucesso")
    void deveCriarTutorComSucesso() {
        TutorCreateDto request = new TutorCreateDto(
                "João Silva",
                "joao@email.com",
                "11999999999"
        );

        ResponseEntity<TutorResponseDto> response = restTemplate.postForEntity(
                "/api/tutores",
                request,
                TutorResponseDto.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().nome()).isEqualTo(("João Silva"));
        assertThat(tutorRepository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("Deve buscar um tutor por ID existente")
    void deveBuscarTutorPorIdExistente() {
        Tutor tutor = new Tutor();
        tutor.setNome("João Silva");
        tutor.setEmail("joao@email.com");
        tutor.setTelefone("11999999999");
        Tutor tutorSalvo = tutorRepository.save(tutor);

        ResponseEntity<TutorResponseDto> response = restTemplate.getForEntity(
                "/api/tutores/" + tutorSalvo.getId(),
                TutorResponseDto.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().nome()).isEqualTo(("João Silva"));
    }

    @Test
    @DisplayName("Deve retornar 404 ao buscar tutor inexistente")
    void deveRetornar404AoBuscarTutorInexistente() {

        ResponseEntity<TutorResponseDto> response = restTemplate.getForEntity(
                "/api/tutores/" + 999,
                TutorResponseDto.class
        );

        assertThat(response.getStatusCode()).isEqualTo((HttpStatus.NOT_FOUND));
    }

    @Test
    @DisplayName("Deve listar todos os tutores existentes")
    void deveListarTodosTutoresExistentes() {
        Tutor tutor1 = new Tutor();
        tutor1.setNome("João Silva");
        tutor1.setEmail("joao@mail.com");
        tutor1.setTelefone("11999999999");

        Tutor tutor2 = new Tutor();
        tutor2.setNome("Pedro Alvares");
        tutor2.setEmail("pedro@mail.com");
        tutor2.setTelefone("65999999999");

        Tutor tutor3 = new Tutor();
        tutor3.setNome("José Bonifácio");
        tutor3.setEmail("jose@mail.com");
        tutor3.setTelefone("21999999999");

        tutorRepository.save(tutor1);
        tutorRepository.save(tutor2);
        tutorRepository.save(tutor3);

        ResponseEntity<TutorResponseDto[]> response = restTemplate.getForEntity(
                "/api/tutores",
                TutorResponseDto[].class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(3);
    }

    @Test
    @DisplayName("Atualizar um tutor existente")
    void deveAtualizarTutorExistente() {
        Tutor tutor = new Tutor();
        tutor.setNome("João Silva");
        tutor.setEmail("joao@mail.com");
        tutor.setTelefone("000000000");
        Tutor tutorSalvo = tutorRepository.save(tutor);


        TutorUpdateDto dadosAtualizado = new TutorUpdateDto(
                "João Atualizado",
                "emailAtualizado@mail.com",
                "111111111");

        HttpEntity<TutorUpdateDto> requestEntity = new HttpEntity<>(dadosAtualizado);

        ResponseEntity<TutorResponseDto> response = restTemplate.exchange(
                "/api/tutores/" + tutorSalvo.getId(),
                HttpMethod.PUT,
                requestEntity,
                TutorResponseDto.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().nome()).isEqualTo("João Atualizado");
        assertThat(response.getBody().email()).isEqualTo("emailAtualizado@mail.com");
        assertThat(response.getBody().telefone()).isEqualTo("111111111");
    }

    @Test
    @DisplayName("Atualizar Tutor inexistente")
    void deveAtualizarTutorInexistente() {
        TutorUpdateDto dadosAtualizado = new TutorUpdateDto(
                "João Atualizado",
                "emailAtualizado@mail.com",
                "111111111");

        HttpEntity<TutorUpdateDto> requestEntity = new HttpEntity<>(dadosAtualizado);

        ResponseEntity<TutorResponseDto> response = restTemplate.exchange(
                "/api/tutores/" + 999,
                HttpMethod.PUT,
                requestEntity,
                TutorResponseDto.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("Deletar um tutor existente")
    void deveDeletarTutorExistente() {
        Tutor tutor = new Tutor();
        tutor.setNome("João Silva");
        tutor.setEmail("joao@mail.com");
        tutor.setTelefone("21999999999");
        Tutor tutorSalvo = tutorRepository.save(tutor);

        ResponseEntity<TutorResponseDto> response = restTemplate.exchange(
                "/api/tutores/" + tutorSalvo.getId(),
                HttpMethod.DELETE,
                null,
                TutorResponseDto.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(tutorRepository.findById(tutorSalvo.getId())).isEmpty();
    }

    @Test
    @DisplayName("Deletar Tutor inexistente")
    void deveDeletarTutorInexistente() {
        ResponseEntity<TutorResponseDto> response = restTemplate.exchange(
                "/api/tutores/" + 999,
                HttpMethod.DELETE,
                null,
                TutorResponseDto.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("Criar Tutor com nome inválido")
    void deveCriarTutorComNomeInvalido() {
        TutorCreateDto request = new TutorCreateDto(
                "",
                "joao@mail.com",
                "11999999999"
        );

        ResponseEntity<TutorResponseDto> response = restTemplate.postForEntity(
                "/api/tutores",
                request,
                TutorResponseDto.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("Criar Tutor com email inválido")
    void deveCriarTutorComEmailInvalido() {
        TutorCreateDto request = new TutorCreateDto(
                "João Silva",
                "joao",
                "11999999999"
        );
        ResponseEntity<TutorResponseDto> response = restTemplate.postForEntity(
                "/api/tutores",
                request,
                TutorResponseDto.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("Criar Tutor com telefone inválido")
    void deveCriarTutorComTelefoneInvalido() {
        TutorCreateDto request = new TutorCreateDto(
                "João Silva",
                "joao@mail.com",
                ""
        );
        ResponseEntity<TutorResponseDto> response = restTemplate.postForEntity(
                "/api/tutores",
                request,
                TutorResponseDto.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}