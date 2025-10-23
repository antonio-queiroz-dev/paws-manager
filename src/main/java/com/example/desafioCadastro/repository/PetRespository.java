package com.example.desafioCadastro.repository;

import com.example.desafioCadastro.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetRespository extends JpaRepository<Pet, Long> {

}
