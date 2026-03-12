package com.example.gestaoPetApi.dto;

import com.example.gestaoPetApi.model.PetSexo;
import com.example.gestaoPetApi.model.PetTipo;

import java.math.BigDecimal;

public record PetResponseDto(Long id,
                             String nomePet,
                             PetTipo petTipo,
                             PetSexo petSexo,
                             Integer idade,
                             BigDecimal peso,
                             String raca,
                             Long tutorId) { }
