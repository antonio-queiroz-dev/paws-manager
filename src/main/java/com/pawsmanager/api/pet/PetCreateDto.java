package com.pawsmanager.api.pet;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record PetCreateDto(
        @Schema(example = "Thor")
        @NotBlank(message = "O nome não pode estar em branco")
        @Pattern(
                regexp = "^[A-Za-zÀ-ÿ]+(\\s[A-Za-zÀ-ÿ]+)*$",
                message = "O nome não pode ter caracteres especiais"
        )
        String nomePet,

        @Schema(example = "CACHORRO")
        PetType petType,
        @Schema(example = "MACHO")
        PetGender petGender,

        @Schema(example = "15")
        @Max(value = 20, message = "A idade não pode ser maior que 20")
        Integer idade,

        @Schema(example = "5.5")
        @DecimalMin(value = "0.5", message = "O peso não pode ser menor que 0.5")
        @DecimalMax(value = "60", message = "O peso não pode ser maior que 60")
        BigDecimal peso,

        @Schema(example = "Caramelo")
        @Pattern(
                regexp = "^[A-Za-zÀ-ÿ]+(\\s[A-Za-zÀ-ÿ]+)*$",
                message = "A raça não pode conter caracteres especiais"
        )
        String raca,

        @Schema(example = "1")
        Long tutorId) {
}