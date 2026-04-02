package com.pawsmanager.api.tutor;


import io.swagger.v3.oas.annotations.media.Schema;

public record TutorResponseDto(
        @Schema(example = "1")
        Long id,
        @Schema(example = "João Silva")
        String nome,
        @Schema(example = "joaosilva@email.com")
        String email,
        @Schema(example = "1234567890")
        String telefone,
        AddressTutor enderecoTutor) {
}
