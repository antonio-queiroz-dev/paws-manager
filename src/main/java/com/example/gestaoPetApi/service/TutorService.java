package com.example.gestaoPetApi.service;

import com.example.gestaoPetApi.dto.TutorCreateDto;
import com.example.gestaoPetApi.dto.TutorResponseDto;
import com.example.gestaoPetApi.dto.TutorUpdateDto;
import com.example.gestaoPetApi.exceptions.RecursoNaoEcontradoException;
import com.example.gestaoPetApi.model.Tutor;
import com.example.gestaoPetApi.repository.TutorRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TutorService {

    private TutorRepository tutorRepository;

    public TutorService(TutorRepository tutorRepository) {
        this.tutorRepository = tutorRepository;
    }

    @Cacheable(value = "tutorList", key = "'all'")
    public List<TutorResponseDto> listarTutores() {
        return tutorRepository.findAll()
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    @CacheEvict(value = "tutorList", allEntries = true)
    public Tutor registrarTutor(TutorCreateDto tutorCreateDto) {
        Tutor tutor = new Tutor();

        tutor.setNome(tutorCreateDto.nome());
        tutor.setEmail(tutorCreateDto.email());
        tutor.setTelefone(tutorCreateDto.telefone());
        tutor.setEnderecoTutor(tutorCreateDto.enderecoTutor());

        return tutorRepository.save(tutor);
    }

    @Caching(evict = {
            @CacheEvict(value = "tutor", key = "#id"),
            @CacheEvict(value = "tutorList", allEntries = true)
    })
    public Tutor updateTutor(Long id, TutorUpdateDto tutorUpdateDto) {
        Optional<Tutor> optionalTutor = tutorRepository.findById(id);

        if (optionalTutor.isEmpty()) {
            throw new RecursoNaoEcontradoException("Tutor com o ID:" + id + " não encontrado");
        }

        Tutor tutorExistente = optionalTutor.get();

        if (tutorUpdateDto.nome() != null) {
            tutorExistente.setNome(tutorUpdateDto.nome());
        }
        if (tutorUpdateDto.email() != null) {
            tutorExistente.setEmail(tutorUpdateDto.email());
        }
        if (tutorUpdateDto.telefone() != null) {
            tutorExistente.setTelefone(tutorUpdateDto.telefone());
        }
        if (tutorUpdateDto.enderecoTutor() != null) {
            tutorExistente.setEnderecoTutor(tutorUpdateDto.enderecoTutor());
        }

        return tutorRepository.save(tutorExistente);
    }

    @Cacheable(value = "tutorList", key = "'nome:' + #nome")
    public List<TutorResponseDto> buscarPorNome(String nome) {
        List<Tutor> listaRetorno;

        if (nome == null || nome.isBlank()) {
            listaRetorno = tutorRepository.findAll();
        } else {
            listaRetorno = tutorRepository.findByNomeContainingIgnoreCase(nome);
        }
        return listaRetorno.stream().map(this::toResponseDto).toList();
    }

    @Cacheable(value = "tutorList", key = "'email:' + #email")
    public List<TutorResponseDto> buscarPorEmail(String email) {
        List<Tutor> listaRetorno;

        if (email == null || email.isBlank()) {
            listaRetorno = tutorRepository.findAll();
        } else {
            listaRetorno = tutorRepository.findByEmailContainingIgnoreCase(email);
        }

        return listaRetorno.stream().map(this::toResponseDto).toList();
    }

    @Cacheable(value = "tutor", key = "#id")
    public Tutor buscarPorId(Long id) {
        return tutorRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEcontradoException("Tutor com o Id:" + id + " não encontrado"));
    }


    @Caching(evict = {
            @CacheEvict(value = "tutor", key = "#id"),
            @CacheEvict(value = "tutorList", allEntries = true)
    })
    public void deletarTutor(Long id) {

        if (!tutorRepository.existsById(id)) {
            throw new RecursoNaoEcontradoException("Tutor com o Id:" + id + " não encontrado");
        }
        tutorRepository.deleteById(id);
    }

    private TutorResponseDto toResponseDto(Tutor tutor) {
        return new TutorResponseDto(
                tutor.getId(),
                tutor.getNome(),
                tutor.getEmail(),
                tutor.getTelefone(),
                tutor.getEnderecoTutor());
    }
}