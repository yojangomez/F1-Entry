package com.crud.demo.repository;

import com.crud.demo.model.Circuit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CircuitRepository extends JpaRepository<Circuit, Integer> {
}