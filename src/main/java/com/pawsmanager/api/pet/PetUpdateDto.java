package com.pawsmanager.api.pet;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;

public record PetUpdateDto(
        @Schema(example = "Thor")
        @Pattern(
                regexp = "^[A-Za-zÀ-ÿ]+(\\s[A-Za-zÀ-ÿ]+)+$",
                message = "O nome deve conter nome e sobrenome, e não pode ter caracteres especiais"
        )
        String nomePet,

        @Schema(example = "7")
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
        String raca ) {
}
