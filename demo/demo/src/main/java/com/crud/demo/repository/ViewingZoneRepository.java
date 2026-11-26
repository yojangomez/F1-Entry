// ViewingZoneRepository.java
package com.crud.demo.repository;

import com.crud.demo.model.ViewingZone;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ViewingZoneRepository extends JpaRepository<ViewingZone, Integer> {
    List<ViewingZone> findByCircuitCircuitId(Integer circuitId);
}