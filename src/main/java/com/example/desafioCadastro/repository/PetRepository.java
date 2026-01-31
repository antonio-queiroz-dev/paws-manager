package com.example.desafioCadastro.repository;

import com.example.desafioCadastro.model.Pet;
import com.example.desafioCadastro.model.PetSexo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

    List<Pet> findByNomePetContainingIgnoreCase(String nome);
    List<Pet> findByPetSexo(PetSexo sexo);
    List<Pet> findByIdadeContainingIgnoreCase(String idade);
    List<Pet> findByTutorId(Long tutorId);

}
