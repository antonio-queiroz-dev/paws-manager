package com.example.gestaoPetApi.integration;

import com.example.gestaoPetApi.dto.PetCreateDto;
import com.example.gestaoPetApi.dto.PetResponseDto;
import com.example.gestaoPetApi.dto.PetUpdateDto;
import com.example.gestaoPetApi.model.*;
import com.example.gestaoPetApi.repository.PetRepository;
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

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;


public class PetIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TutorRepository tutorRepository;

    @Autowired
    private PetRepository petRepository;

    private Tutor tutorPadrao;
    private static final String BASE_URL = "/api/pets";

    @BeforeEach
    void setUp() {
        petRepository.deleteAll();
        tutorRepository.deleteAll();

        tutorPadrao = criarEsalvarTutor("João Silva", "joao@email.com", "11999999999");
    }

    private Tutor criarEsalvarTutor(String nome, String email, String telefone) {
        Tutor tutor = new Tutor();
        tutor.setNome(nome);
        tutor.setEmail(email);
        tutor.setTelefone(telefone);
        return tutorRepository.save(tutor);
    }

    private Pet criarESalvarPet(String nome, PetTipo tipo, PetSexo sexo, Integer idade,
                                BigDecimal peso, String raca, Tutor tutor) {
        Pet pet = new Pet();
        pet.setNomePet(nome);
        pet.setPetTipo(tipo);
        pet.setPetSexo(sexo);
        pet.setIdade(idade);
        pet.setPeso(peso);
        pet.setRaca(raca);
        pet.setTutor(tutor);
        return petRepository.save(pet);
    }

    private PetCreateDto criarPetCreatDto(String nome, Long tutorId) {
        return new PetCreateDto(
                nome,
                PetTipo.GATO,
                PetSexo.MACHO,
                5,
                new BigDecimal("4.0"),
                "Siames",
                tutorId
        );
    }

    private PetUpdateDto criarPetUpdateDto(String nome, Integer idade, BigDecimal peso, String raca) {
        return new PetUpdateDto(
                nome,
                idade,
                peso,
                raca
        );
    }

    @Test
    @DisplayName("Deve criar um pet com sucesso")
    void deveCriarPetComSucesso() {

        PetCreateDto request = criarPetCreatDto(
                "José caça rato",
                tutorPadrao.getId()
        );

        ResponseEntity<PetResponseDto> response = restTemplate.postForEntity(
                BASE_URL,
                request,
                PetResponseDto.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().nomePet()).isEqualTo(("José caça rato"));
        assertThat(response.getBody().petTipo()).isEqualTo(PetTipo.GATO);

        assertThat(petRepository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("Deve retornar 404 criar pet com tutor inexistente")
    void deveRetornar404CriarPetComTutorInexistente() {


        PetCreateDto request = criarPetCreatDto(
                "José caça rato",
                999L
        );

        ResponseEntity<PetResponseDto> response = restTemplate.postForEntity(
                BASE_URL,
                request,
                PetResponseDto.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(petRepository.count()).isEqualTo(0);
    }

    @Test
    @DisplayName("Criar Pet com dados invalidos")
    void deveCriarPetComDadosInvalidos() {

        PetCreateDto request = new PetCreateDto(
                "",
                PetTipo.GATO,
                PetSexo.MACHO,
                25,
                new BigDecimal("70.0"),
                "Siames",
                999L
        );
        ResponseEntity<PetResponseDto> response = restTemplate.postForEntity(
                BASE_URL,
                request,
                PetResponseDto.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("Deve buscar um pet por ID existente")
    void deveBuscarPetPorIdExistente() {

        Pet petSalvo = criarESalvarPet(
                "José caça rato",
                PetTipo.GATO,
                PetSexo.MACHO,
                5,
                new BigDecimal("4.0"),
                "Siames",
                tutorPadrao
        );


        ResponseEntity<PetResponseDto> response = restTemplate.getForEntity(
                BASE_URL + "/" + petSalvo.getId(),
                PetResponseDto.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().nomePet()).isEqualTo(("José caça rato"));
    }

    @Test
    @DisplayName("Deve retornar 404 ao buscar pet inexistente")
    void deveRetornar404AoBuscarPetInexistente() {

        ResponseEntity<PetResponseDto> response = restTemplate.getForEntity(
                BASE_URL + "/" + 999,
                PetResponseDto.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("Verificar se retorna todos os pets")
    void deveRetornarTodosPets() {

        Tutor tutor2 = criarEsalvarTutor(
                "Maria dos Santos",
                "maria@email.com",
                "11235467895");

        criarESalvarPet(
                "José caça rato",
                PetTipo.GATO, PetSexo.MACHO,
                5,
                new BigDecimal("4.0"),
                "Siames",
                tutorPadrao
        );

        criarESalvarPet(
                "Mailina Santos",
                PetTipo.GATO,
                PetSexo.FEMEA,
                8,
                new BigDecimal("5.0"),
                "Siames",
                tutor2
        );

        ResponseEntity<PetResponseDto[]> response = restTemplate.getForEntity(
                BASE_URL,
                PetResponseDto[].class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(2);
        assertThat(response.getBody())
                .extracting(PetResponseDto::nomePet)
                .containsExactlyInAnyOrder("José caça rato", "Mailina Santos");
    }

    @Test
    @DisplayName("Buscar pet de um tutor especifico")
    void deveBuscarPetDeUmTutorEspecifico() {
        Tutor tutor2 = criarEsalvarTutor("Maria dos Santos", "maria@email.com", "11235467895");

        criarESalvarPet("José caça rato",
                PetTipo.GATO,
                PetSexo.MACHO,
                5,
                new BigDecimal("4.0"),
                "Siames",
                tutorPadrao);
        criarESalvarPet(
                "Thor Silva",
                PetTipo.GATO,
                PetSexo.MACHO,
                5,
                new BigDecimal("4.0"),
                "Siames",
                tutorPadrao);

        criarESalvarPet(
                "Marilina",
                PetTipo.GATO,
                PetSexo.FEMEA,
                8,
                new BigDecimal("5.0"),
                "Siames", tutor2);

        ResponseEntity<PetResponseDto[]> response = restTemplate.getForEntity(
                BASE_URL + "/tutor/" + tutorPadrao.getId(),
                PetResponseDto[].class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(2);
        assertThat(response.getBody())
                .extracting(PetResponseDto::nomePet)
                .containsExactlyInAnyOrder("José caça rato", "Thor Silva");
    }

    @Test
    @DisplayName("Atualizar dados do Pet")
    void deveAtualizarDadosPet() {
        Pet petSalvo = criarESalvarPet(
                "José caça rato",
                PetTipo.GATO,
                PetSexo.MACHO,
                5,
                new BigDecimal("4.0"),
                "Siames",
                tutorPadrao
        );

        PetUpdateDto dadosAtualizados = criarPetUpdateDto(
                "José atualizado",
                6,
                new BigDecimal("7"),
                "Persa"
        );

        HttpEntity<PetUpdateDto> requestEntity = new HttpEntity<>(dadosAtualizados);

        ResponseEntity<PetResponseDto> response = restTemplate.exchange(
                BASE_URL+ "/" + petSalvo.getId(),
                HttpMethod.PUT,
                requestEntity,
                PetResponseDto.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().nomePet()).isEqualTo("José atualizado");
        assertThat(response.getBody().raca()).isEqualTo("Persa");
        assertThat(response.getBody().idade()).isEqualTo(6);
        assertThat(response.getBody().peso()).isEqualByComparingTo(new BigDecimal("7.0"));
    }

    @Test
    @DisplayName("Atualizar pet inexistente")
    void deveAtualizarPetInexistente() {

        PetUpdateDto dadosAtualizados = criarPetUpdateDto(
                "José atualizado",
                6,
                new BigDecimal("7"),
                "Persa"
        );

        HttpEntity<PetUpdateDto> requestEntity = new HttpEntity<>(dadosAtualizados);

        ResponseEntity<PetResponseDto> response = restTemplate.exchange(
                BASE_URL + "/" + 999,
                HttpMethod.PUT,
                requestEntity,
                PetResponseDto.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("Deletar pet existente")
    void deveDeletarPet() {

        Pet petSalvo = criarESalvarPet(
                "José caça rato",
                PetTipo.GATO,
                PetSexo.MACHO,
                5,
                new BigDecimal("4.0"),
                "Siames",
                tutorPadrao
        );

        Long petId = petSalvo.getId();

        ResponseEntity<PetResponseDto> response = restTemplate.exchange(
                BASE_URL + "/" + petId,
                HttpMethod.DELETE,
                null,
                PetResponseDto.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(petRepository.findById(petId)).isEmpty();
        assertThat(petRepository.count()).isEqualTo(0);
    }

    @Test
    @DisplayName("Deletar pet inexistente")
    void deveDeletarPetInexistente() {
        ResponseEntity<PetResponseDto> response = restTemplate.exchange(
                BASE_URL + "/" + 999,
                HttpMethod.DELETE,
                null,
                PetResponseDto.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}