package com.example.bugsystem.repositories;

import com.example.bugsystem.model.Programmer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IProgrammerRepository extends JpaRepository<Programmer, Integer> {
    Optional<Programmer> findProgrammerByUsername(String username);
}
