package com.pawsmanager.api.pet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

    List<Pet> findByNomePetContainingIgnoreCase(String nome);
    List<Pet> findByPetGender(PetGender sexo);
    List<Pet> findByIdade(Integer idade);
    List<Pet> findByTutorId(Long tutorId);

}
