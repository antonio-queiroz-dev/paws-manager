package com.example.gestaoPetApi.dto;


import com.example.gestaoPetApi.model.EnderecoTutor;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record TutorCreateDto(
        @NotBlank(message = "O nome não pode estar em branco")
        String nome,

        @NotBlank(message = "O email não pode estar em branco")
        @Email(message = "Formato de email inválido")
        String email,

        @NotBlank(message = "O telefone não pode estar em branco")

        @Pattern(regexp = "\\d{10,11}",message = "O telefone deve ter de 10 a 11 dígitos")
        String telefone,

        EnderecoTutor enderecoTutor
        ) {
}
