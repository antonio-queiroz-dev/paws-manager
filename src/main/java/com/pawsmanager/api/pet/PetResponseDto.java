package com.pawsmanager.api.pet;

import java.math.BigDecimal;

public record PetResponseDto(Long id,
                             String nomePet,
                             PetType petType,
                             PetGender petGender,
                             Integer idade,
                             BigDecimal peso,
                             String raca,
                             Long tutorId) { }
