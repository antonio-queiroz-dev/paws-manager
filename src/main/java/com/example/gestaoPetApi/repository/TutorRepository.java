package com.example.gestaoPetApi.repository;

import com.example.gestaoPetApi.model.Tutor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TutorRepository extends JpaRepository<Tutor, Long> {
    List<Tutor> findByNomeContainingIgnoreCase(String nome);
    List<Tutor> findByEmailContainingIgnoreCase(String email);

}
