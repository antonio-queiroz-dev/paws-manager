package com.pawsmanager.api.pet;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record PetResponseDto(
        @Schema(example = "1")
        Long id,
        @Schema(example = "Thor")
        String nomePet,
        @Schema(example = "CACHORRO")
        PetType petType,
        @Schema(example = "MACHO")
        PetGender petGender,
        @Schema(example = "15")
        Integer idade,
        @Schema(example = "5.5")
        BigDecimal peso,
        @Schema(example = "Caramelo")
        String raca,
        @Schema(example = "1")
        Long tutorId) {
}
