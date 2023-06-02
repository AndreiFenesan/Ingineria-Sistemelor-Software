package com.example.bugsystem.repositories;

import com.example.bugsystem.model.Tester;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ITesterRepository extends JpaRepository<Tester, Integer> {
    Optional<Tester> findTesterByUsername(String username);
}
