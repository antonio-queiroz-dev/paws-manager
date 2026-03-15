package com.example.gestaoPetApi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthRequest(
        @Email(message = "Email invalido")
        @NotBlank(message = "Email é obrigatorio")
        String email,

        @NotBlank(message = "Senha é obrigatoria")
        String password
) {
}
