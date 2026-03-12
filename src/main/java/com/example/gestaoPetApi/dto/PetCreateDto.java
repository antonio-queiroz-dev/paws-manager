package com.example.gestaoPetApi.dto;

import com.example.gestaoPetApi.model.EnderecoTutor;
import com.example.gestaoPetApi.model.PetSexo;
import com.example.gestaoPetApi.model.PetTipo;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record PetCreateDto(
        @Pattern(
                regexp = "^[A-Za-zÀ-ÿ]+(\\s[A-Za-zÀ-ÿ]+)*$",
                message = "O nome não pode ter caracteres especiais"
        )
        String nomePet,

        PetTipo petTipo,
        PetSexo petSexo,

        @Max(value = 20, message = "A idade não pode ser maior que 20")
        Integer idade,

        @DecimalMin(value = "0.5", message = "O peso não pode ser menor que 0.5")
        @DecimalMax(value = "60", message = "O peso não pode ser maior que 60")
        BigDecimal peso,

        @Pattern(
                regexp = "^[A-Za-zÀ-ÿ]+(\\s[A-Za-zÀ-ÿ]+)*$",
                message = "A raça não pode conter caracteres especiais"
        )
        String raca,

        Long tutorId) {
}