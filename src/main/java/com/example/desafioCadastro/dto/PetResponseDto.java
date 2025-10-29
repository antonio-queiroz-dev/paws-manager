package com.example.desafioCadastro.dto;

import com.example.desafioCadastro.model.PetEndereco;
import com.example.desafioCadastro.model.PetSexo;
import com.example.desafioCadastro.model.PetTipo;

public record PetResponseDto(Long id,
                             String nomePet,
                             PetTipo petTipo,
                             PetSexo petSexo,
                             PetEndereco petEndereco,
                             String idade,
                             String peso,
                             String raca) {
}
