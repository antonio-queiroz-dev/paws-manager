package com.pawsmanager.api.tutor;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TutorRepository extends JpaRepository<Tutor, Long> {
    List<Tutor> findByNomeContainingIgnoreCase(String nome);
    List<Tutor> findByEmailContainingIgnoreCase(String email);

}
