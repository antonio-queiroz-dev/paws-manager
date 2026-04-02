package com.pawsmanager.api.tutor;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

public record TutorUpdateDto(
        @Schema(example = "João Silva")
        String nome,

        @Schema(example = "joaosilva@email.com")
        @Email(message = "Formato de email inválido")
        String email,

        @Schema(example = "1234567890")
        @Pattern(regexp = "\\d{10,11}",message = "O telefone deve ter de 10 a 11 dígitos")
        String telefone,

        AddressTutor addressTutor) {
}
