package com.example.desafioCadastro.service;

import com.example.desafioCadastro.dto.TutorCreateDto;
import com.example.desafioCadastro.dto.TutorResponseDto;
import com.example.desafioCadastro.dto.TutorUpdateDto;
import com.example.desafioCadastro.exceptions.RecursoNaoEcontradoException;
import com.example.desafioCadastro.model.Tutor;
import com.example.desafioCadastro.repository.TutorRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TutorService {

    private TutorRepository tutorRepository;

    public TutorService(TutorRepository tutorRepository) {
        this.tutorRepository = tutorRepository;
    }

    public List<TutorResponseDto> listarTutores() {
        return tutorRepository.findAll()
                .stream().map(this::toResponseDto).toList();
    }

    private TutorResponseDto toResponseDto(Tutor tutor) {
        return new TutorResponseDto(
                tutor.getId(),
                tutor.getNome(),
                tutor.getEmail(),
                tutor.getTelefone());
    }

    @CacheEvict(value = "tutores", allEntries = true)
    public Tutor registrarTutor(TutorCreateDto tutorCreateDto) {
        Tutor tutor = new Tutor();

        tutor.setNome(tutorCreateDto.nome());
        tutor.setEmail(tutorCreateDto.email());
        tutor.setTelefone(tutorCreateDto.telefone());

        return tutorRepository.save(tutor);
    }

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

        return tutorRepository.save(tutorExistente);
    }

    public List<TutorResponseDto> buscarPorNome(String nome) {
        List<Tutor> listaRetorno;

        if (nome == null || nome.isBlank()) {
            listaRetorno = tutorRepository.findAll();
        }

        listaRetorno = tutorRepository.findByNomeContainingIgnoreCase(nome);
        return listaRetorno.stream().map(this::toResponseDto).toList();
    }

    public List<TutorResponseDto> buscarPorEmail(String email) {
        List<Tutor> listaRetorno;

        if (email == null || email.isBlank()) {
            listaRetorno = tutorRepository.findAll();
        }

        listaRetorno = tutorRepository.findByEmailContainingIgnoreCase(email);
        return listaRetorno.stream().map(this::toResponseDto).toList();
    }


    @CacheEvict(value = "tutor", allEntries = true)
    public void deletarTutor(Long id) {

        if (!tutorRepository.existsById(id)) {
            throw new RecursoNaoEcontradoException("Tutor com o Id:" + id + " não encontrado");
        }
        tutorRepository.deleteById(id);
    }
}