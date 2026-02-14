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

    @BeforeEach
    void setUp() {
        petRepository.deleteAll();
        tutorRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve criar um pet com sucesso")
    void deveCriarPetComSucesso() {
        Tutor tutor = new Tutor();
        tutor.setNome("João Silva");
        tutor.setEmail("joao@email.com");
        tutor.setTelefone("11999999999");
        Tutor tutorSalvo = tutorRepository.save(tutor);

        PetEndereco endereco = new PetEndereco();
        endereco.setRua("Rua das Acácias");
        endereco.setCidade("Belo Horizonte");
        endereco.setNumeroCasa("55");


        PetCreateDto request = new PetCreateDto(
                "José caça rato",
                PetTipo.GATO,
                PetSexo.MACHO,
                endereco,
                5,
                new BigDecimal("4.0"),
                "Siames",
                tutorSalvo.getId()
        );

        ResponseEntity<PetResponseDto> response = restTemplate.postForEntity(
                "/api/pets",
                request,
                PetResponseDto.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().nomePet()).isEqualTo(("José caça rato"));
    }

    @Test
    @DisplayName("Deve retornar 404 criar pet com tutor inexistente")
    void deveRetornar404CriarPetComTutorInexistente() {

        PetEndereco endereco = new PetEndereco();
        endereco.setRua("Rua das Acácias");
        endereco.setCidade("Belo Horizonte");
        endereco.setNumeroCasa("55");

        PetCreateDto request = new PetCreateDto(
                "José caça rato",
                PetTipo.GATO,
                PetSexo.MACHO,
                endereco,
                5,
                new BigDecimal("4.0"),
                "Siames",
                999L
        );

        ResponseEntity<PetResponseDto> response = restTemplate.postForEntity(
                "/api/pets",
                request,
                PetResponseDto.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }

    @Test
    @DisplayName("Criar Pet com dados invalidos")
    void deveCriarPetComDadosInvalidos() {
        PetEndereco endereco = new PetEndereco();
        endereco.setRua("Rua das Acácias");
        endereco.setCidade("Belo Horizonte");
        endereco.setNumeroCasa("55");
        PetCreateDto request = new PetCreateDto(
                "",
                PetTipo.GATO,
                PetSexo.MACHO,
                endereco,
                25,
                new BigDecimal("70.0"),
                "Siames",
                999L
        );
        ResponseEntity<PetResponseDto> response = restTemplate.postForEntity(
                "/api/pets",
                request,
                PetResponseDto.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("Deve buscar um pet por ID existente")
    void deveBuscarPetPorIdExistente() {
        Tutor tutor = new Tutor();
        tutor.setNome("João Silva");
        tutor.setEmail("joao@email.com");
        tutor.setTelefone("11999999999");
        Tutor tutorSalvo = tutorRepository.save(tutor);

        PetEndereco endereco = new PetEndereco();
        endereco.setRua("Rua das Acácias");
        endereco.setCidade("Belo Horizonte");
        endereco.setNumeroCasa("55");

        Pet petSalvo = new Pet();
        petSalvo.setNomePet("José caça rato");
        petSalvo.setPetTipo(PetTipo.GATO);
        petSalvo.setPetSexo(PetSexo.MACHO);
        petSalvo.setPetEndereco(endereco);
        petSalvo.setIdade(5);
        petSalvo.setPeso(new BigDecimal("4.0"));
        petSalvo.setRaca("Siames");
        petSalvo.setTutor(tutorSalvo);
        petRepository.save(petSalvo);


        ResponseEntity<PetResponseDto> response = restTemplate.getForEntity(
                "/api/pets/" + petSalvo.getId(),
                PetResponseDto.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().nomePet()).isEqualTo(("José caça rato"));
    }

    @Test
    @DisplayName("Verificar se retorna todos os pets")
    void deveRetornarTodosPets() {
        Tutor tutor1 = new Tutor();
        tutor1.setNome("João Silva");
        tutor1.setEmail("joao@email.com");
        tutor1.setTelefone("11999999999");
        Tutor tutorSalvo = tutorRepository.save(tutor1);

        PetEndereco endereco = new PetEndereco();
        endereco.setRua("Rua das Acácias");
        endereco.setCidade("Belo Horizonte");
        endereco.setNumeroCasa("55");

        Pet pet1Tutor1 = new Pet();
        pet1Tutor1.setNomePet("José caça rato");
        pet1Tutor1.setPetTipo(PetTipo.GATO);
        pet1Tutor1.setPetSexo(PetSexo.MACHO);
        pet1Tutor1.setPetEndereco(endereco);
        pet1Tutor1.setIdade(5);
        pet1Tutor1.setPeso(new BigDecimal("4.0"));
        pet1Tutor1.setRaca("Siames");
        pet1Tutor1.setTutor(tutorSalvo);

        petRepository.save(pet1Tutor1);

        Tutor tutor2 = new Tutor();
        tutor2.setNome("Maria dos Santos");
        tutor2.setEmail("maria@email.com");
        tutor2.setTelefone("11235467895");
        Tutor tutorSalvo2 = tutorRepository.save(tutor2);

        PetEndereco endereco2 = new PetEndereco();
        endereco2.setRua("Rua Osni Lopes");
        endereco2.setCidade("Jaraguá do Sul");
        endereco2.setNumeroCasa("87");

        Pet pet1tutor2 = new Pet();
        pet1tutor2.setNomePet("Marilina Santos");
        pet1tutor2.setPetTipo(PetTipo.GATO);
        pet1tutor2.setPetSexo(PetSexo.FEMEA);
        pet1tutor2.setPetEndereco(endereco2);
        pet1tutor2.setIdade(8);
        pet1tutor2.setPeso(new BigDecimal("5.0"));
        pet1tutor2.setRaca("Siames");
        pet1tutor2.setTutor(tutorSalvo2);

        petRepository.save(pet1tutor2);

        ResponseEntity<PetResponseDto[]> response = restTemplate.getForEntity(
                "/api/pets",
                PetResponseDto[].class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(2);
    }

    @Test
    @DisplayName("Buscar pet de um tutor especifico")
    void deveBuscarPetDeUmTutorEspecifico() {
        Tutor tutor1 = new Tutor();
        tutor1.setNome("João Silva");
        tutor1.setEmail("joao@email.com");
        tutor1.setTelefone("11999999999");
        Tutor tutorSalvo = tutorRepository.save(tutor1);

        PetEndereco endereco = new PetEndereco();
        endereco.setRua("Rua das Acácias");
        endereco.setCidade("Belo Horizonte");
        endereco.setNumeroCasa("55");

        Pet pet1Tutor1 = new Pet();
        pet1Tutor1.setNomePet("José caça rato");
        pet1Tutor1.setPetTipo(PetTipo.GATO);
        pet1Tutor1.setPetSexo(PetSexo.MACHO);
        pet1Tutor1.setPetEndereco(endereco);
        pet1Tutor1.setIdade(5);
        pet1Tutor1.setPeso(new BigDecimal("4.0"));
        pet1Tutor1.setRaca("Siames");
        pet1Tutor1.setTutor(tutorSalvo);

        petRepository.save(pet1Tutor1);

        Pet pet2tutor1 = new Pet();
        pet2tutor1.setNomePet("Thor Silva");
        pet2tutor1.setPetTipo(PetTipo.CACHORRO);
        pet2tutor1.setPetSexo(PetSexo.MACHO);
        pet2tutor1.setPetEndereco(endereco);
        pet2tutor1.setIdade(10);
        pet2tutor1.setPeso(new BigDecimal("9.0"));
        pet2tutor1.setRaca("Caramelo");
        pet2tutor1.setTutor(tutorSalvo);

        petRepository.save(pet2tutor1);

        Tutor tutor2 = new Tutor();
        tutor2.setNome("Maria dos Santos");
        tutor2.setEmail("maria@email.com");
        tutor2.setTelefone("11235467895");
        Tutor tutorSalvo2 = tutorRepository.save(tutor2);

        PetEndereco endereco2 = new PetEndereco();
        endereco2.setRua("Rua Osni Lopes");
        endereco2.setCidade("Jaraguá do Sul");
        endereco2.setNumeroCasa("87");

        Pet pet1tutor2 = new Pet();
        pet1tutor2.setNomePet("Marilina");
        pet1tutor2.setPetTipo(PetTipo.GATO);
        pet1tutor2.setPetSexo(PetSexo.FEMEA);
        pet1tutor2.setPetEndereco(endereco2);
        pet1tutor2.setIdade(8);
        pet1tutor2.setPeso(new BigDecimal("5.0"));
        pet1tutor2.setRaca("Siames");
        pet1tutor2.setTutor(tutorSalvo2);

        petRepository.save(pet1tutor2);

        ResponseEntity<PetResponseDto[]> response = restTemplate.getForEntity(
                "/api/pets/tutor/" + tutorSalvo.getId(),
                PetResponseDto[].class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(2);
        assertThat(response.getBody()[0].nomePet()).isIn("José caça rato", "Thor Silva");
        assertThat(response.getBody()[1].nomePet()).isIn("José caça rato", "Thor Silva");
    }

    @Test
    @DisplayName("Atualizar dados do Pet")
    void deveAtualizarDadosPet() {
        Tutor tutor = new Tutor();
        tutor.setNome("João Silva");
        Tutor tutorSalvo = tutorRepository.save(tutor);

        PetEndereco endereco = new PetEndereco();
        endereco.setRua("Rua das Acácias");
        endereco.setCidade("Belo Horizonte");
        endereco.setNumeroCasa("55");

        Pet petSalvo = new Pet();
        petSalvo.setNomePet("José caça rato");
        petSalvo.setPetTipo(PetTipo.GATO);
        petSalvo.setPetSexo(PetSexo.MACHO);
        petSalvo.setPetEndereco(endereco);
        petSalvo.setIdade(5);
        petSalvo.setPeso(new BigDecimal("4"));
        petSalvo.setRaca("Siames");
        petSalvo.setTutor(tutorSalvo);
        Pet petAtualizado = petRepository.save(petSalvo);

        PetEndereco enderecoAtualizado = new PetEndereco();
        enderecoAtualizado.setRua("Rua atualizada");
        enderecoAtualizado.setCidade("cidade atualizada");
        enderecoAtualizado.setNumeroCasa("55");

        PetUpdateDto dadosAtualizados = new PetUpdateDto(
                "José atualizado",
                enderecoAtualizado,
                6,
                new BigDecimal("7"),
                "Persa"
        );
        HttpEntity<PetUpdateDto> requestEntity = new HttpEntity<>(dadosAtualizados);

        ResponseEntity<PetResponseDto> response = restTemplate.exchange(
                "/api/pets/" + petSalvo.getId(),
                HttpMethod.PUT,
                requestEntity,
                PetResponseDto.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().nomePet()).isEqualTo("José atualizado");
        assertThat(response.getBody().petEndereco().getRua().equals("Rua atualizada"));
        assertThat(response.getBody().petEndereco().getCidade().equals("cidade atualizada"));
        assertThat(response.getBody().petEndereco().getNumeroCasa().equals("55"));
        assertThat(response.getBody().raca()).isEqualTo("Persa");
        assertThat(response.getBody().idade()).isEqualTo(6);
        assertThat(response.getBody().peso()).isEqualTo(new BigDecimal("7"));
    }

    @Test
    @DisplayName("Atualizar pet inexistente")
    void deveAtualizarPetInexistente() {
        PetEndereco enderecoAtualizado = new PetEndereco();
        enderecoAtualizado.setRua("Rua das Acácias");
        enderecoAtualizado.setCidade("Belo Horizonte");
        enderecoAtualizado.setNumeroCasa("55");

        PetUpdateDto dadosAtualizados = new PetUpdateDto(
                "José atualizado",
                enderecoAtualizado,
                6,
                new BigDecimal("7"),
                "Persa"
        );

        HttpEntity<PetUpdateDto> requestEntity = new HttpEntity<>(dadosAtualizados);

        ResponseEntity<PetResponseDto> response = restTemplate.exchange(
                "/api/pets/" + 999,
                HttpMethod.PUT,
                requestEntity,
                PetResponseDto.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("Deletar pet existente")
    void deveDeletarPet() {
        Tutor tutor = new Tutor();
        tutor.setNome("João Silva");
        Tutor tutorSalvo = tutorRepository.save(tutor);

        PetEndereco endereco = new PetEndereco();
        endereco.setRua("Rua das Acácias");
        endereco.setCidade("Belo Horizonte");
        endereco.setNumeroCasa("55");

        Pet pet = new Pet();
        pet.setNomePet("José caça rato");
        pet.setPetTipo(PetTipo.GATO);
        pet.setPetSexo(PetSexo.MACHO);
        pet.setPetEndereco(endereco);
        pet.setIdade(5);
        pet.setPeso(new BigDecimal("4"));
        pet.setRaca("Siames");
        pet.setTutor(tutorSalvo);
        Pet petsalvo = petRepository.save(pet);

        ResponseEntity<PetResponseDto> response = restTemplate.exchange(
                "/api/pets/" + petsalvo.getId(),
                HttpMethod.DELETE,
                null,
                PetResponseDto.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(petRepository.findById(pet.getId())).isEmpty();
    }

    @Test
    @DisplayName("Deletar pet inexistente")
    void deveDeletarPetInexistente() {
        ResponseEntity<PetResponseDto> response = restTemplate.exchange(
                "/api/pets/" + 999
                , HttpMethod.DELETE,
                null,
                PetResponseDto.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}