package com.crud.demo.repository;

import com.crud.demo.model.Spectator;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SpectatorRepository extends JpaRepository<Spectator, Integer> {
    Optional<Spectator> findByDocumentNumber(String documentNumber);
}