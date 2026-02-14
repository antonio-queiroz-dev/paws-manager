package com.example.gestaoPetApi.repository;

import com.example.gestaoPetApi.model.Pet;
import com.example.gestaoPetApi.model.PetSexo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

    List<Pet> findByNomePetContainingIgnoreCase(String nome);
    List<Pet> findByPetSexo(PetSexo sexo);
    List<Pet> findByIdade(Integer idade);
    List<Pet> findByTutorId(Long tutorId);

}
