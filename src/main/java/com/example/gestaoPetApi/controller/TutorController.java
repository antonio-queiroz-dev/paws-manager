package com.example.gestaoPetApi.controller;

import com.example.gestaoPetApi.dto.TutorCreateDto;
import com.example.gestaoPetApi.dto.TutorResponseDto;
import com.example.gestaoPetApi.dto.TutorUpdateDto;
import com.example.gestaoPetApi.service.TutorService;
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
        TutorResponseDto responseDto = tutorService.registrarTutor(tutorCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TutorResponseDto> updateTutor(@PathVariable Long id, @Valid @RequestBody TutorUpdateDto tutorUpdateDto) {
        TutorResponseDto responseDto = tutorService.updateTutor(id, tutorUpdateDto);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/buscar/nome")
    public ResponseEntity<List<TutorResponseDto>> buscarPorNome(@RequestParam(required = false) String nome) {
        if (nome == null || nome.isBlank()) {
            return listarTutores();
        }
        List<TutorResponseDto> tutoresList = tutorService.buscarPorNome(nome);
        return ResponseEntity.ok(tutoresList);
    }

    @GetMapping("/buscar/email")
    public ResponseEntity<List<TutorResponseDto>> buscarPorEmail(@RequestParam(required = false) String email) {
        if (email == null || email.isBlank()) {
            return listarTutores();
        }
        List<TutorResponseDto> tutoresList = tutorService.buscarPorEmail(email);
        return ResponseEntity.ok(tutoresList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TutorResponseDto> buscarPorId(@PathVariable Long id) {
        TutorResponseDto tutorResponseDto = tutorService.buscarPorId(id);
        return ResponseEntity.ok(tutorResponseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarTutor(@PathVariable Long id) {
        tutorService.deletarTutor(id);
        return ResponseEntity.noContent().build();
    }
}
