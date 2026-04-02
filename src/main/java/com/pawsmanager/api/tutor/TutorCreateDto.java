package com.pawsmanager.api.tutor;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record TutorCreateDto(
        @Schema(example = "João Silva")
        @NotBlank(message = "O nome não pode estar em branco")
        String nome,

        @Schema(example = "joaosilva@email.com")
        @NotBlank(message = "O email não pode estar em branco")
        @Email(message = "Formato de email inválido")
        String email,

        @Schema(example = "1234567890")
        @NotBlank(message = "O telefone não pode estar em branco")
        @Pattern(regexp = "\\d{10,11}",message = "O telefone deve ter de 10 a 11 dígitos")
        String telefone,

        AddressTutor enderecoTutor
        ) {
}
