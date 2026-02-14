package com.example.gestaoPetApi.dto;

import com.example.gestaoPetApi.model.PetEndereco;

import java.math.BigDecimal;

public record PetUpdateDto(String nomePet,
                           PetEndereco petEndereco,
                           Integer idade,
                           BigDecimal peso,
                           String raca ) {
}