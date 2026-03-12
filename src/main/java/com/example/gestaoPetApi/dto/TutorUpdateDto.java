package com.example.gestaoPetApi.dto;

import com.example.gestaoPetApi.model.EnderecoTutor;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

public record TutorUpdateDto(
        String nome,

        @Email(message = "Formato de email inválido")
        String email,

        @Pattern(regexp = "\\d{10,11}",message = "O telefone deve ter de 10 a 11 dígitos")
        String telefone,

        EnderecoTutor enderecoTutor) {
}
