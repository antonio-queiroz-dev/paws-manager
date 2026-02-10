package com.example.desafioCadastro.controller;

import com.example.desafioCadastro.dto.TutorCreateDto;
import com.example.desafioCadastro.dto.TutorResponseDto;
import com.example.desafioCadastro.dto.TutorUpdateDto;
import com.example.desafioCadastro.model.Tutor;
import com.example.desafioCadastro.service.TutorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/tutores")
public class TutorController {

    private final TutorService tutorService;

    public TutorController(TutorService tutorService) {
        this.tutorService = tutorService;
    }

    @GetMapping
    public ResponseEntity<List<TutorResponseDto>> listarTutores() {
        List<TutorResponseDto> tutores = tutorService.listarTutores();
        return ResponseEntity.ok(tutores);
    }

    @PostMapping
    public ResponseEntity<TutorResponseDto> cadastrarTutor(@Valid @RequestBody TutorCreateDto tutorCreateDto) {
        Tutor novoTutor = tutorService.registrarTutor(tutorCreateDto);

        TutorResponseDto responseDto = new TutorResponseDto(
                novoTutor.getId(),
                novoTutor.getNome(),
                novoTutor.getEmail(),
                novoTutor.getTelefone()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TutorResponseDto> updateTutor(@Valid @PathVariable Long id, @RequestBody TutorUpdateDto tutorUpdateDto) {
        Tutor updateTutor = tutorService.updateTutor(id, tutorUpdateDto);

        TutorResponseDto responseDto = new TutorResponseDto(
                updateTutor.getId(),
                updateTutor.getNome(),
                updateTutor.getEmail(),
                updateTutor.getTelefone()
        );
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/buscar/nome")
    public ResponseEntity<List<TutorResponseDto>> buscarPorNome(@RequestParam String nome) {
        List<TutorResponseDto> tutoresList = tutorService.buscarPorNome(nome);
        return ResponseEntity.ok(tutoresList);
    }

    @GetMapping("/buscar/email")
    public ResponseEntity<List<TutorResponseDto>> buscarPorEmail(@RequestParam String email) {
        List<TutorResponseDto> tutoresList = tutorService.buscarPorEmail(email);
        return ResponseEntity.ok(tutoresList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TutorResponseDto> buscarPorId(@PathVariable Long id) {
        Tutor tutor = tutorService.buscarPorId(id);

        TutorResponseDto tutorResponseDto = new TutorResponseDto(
                tutor.getId(),
                tutor.getNome(),
                tutor.getEmail(),
                tutor.getTelefone()
        );
        return ResponseEntity.ok(tutorResponseDto);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarTutor(@PathVariable Long id) {
        tutorService.deletarTutor(id);
        return ResponseEntity.noContent().build();
    }
}
