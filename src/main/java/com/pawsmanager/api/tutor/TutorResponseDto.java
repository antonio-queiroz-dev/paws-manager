package com.pawsmanager.api.tutor;


public record TutorResponseDto(Long id,
                               String nome,
                               String email,
                               String telefone,
                               AddressTutor enderecoTutor) {
}
