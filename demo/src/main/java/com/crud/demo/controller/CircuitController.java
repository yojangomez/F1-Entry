package com.crud.demo.controller;

import com.crud.demo.model.Circuit;
import com.crud.demo.model.Registration;
import com.crud.demo.model.ViewingZone;
import com.crud.demo.repository.CircuitRepository;
import com.crud.demo.repository.RegistrationRepository;
import com.crud.demo.repository.ViewingZoneRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@RestController
@RequestMapping("/api/circuits")
@CrossOrigin(origins = "*")
public class CircuitController {

    private final CircuitRepository circuitRepository;
    private final ViewingZoneRepository zoneRepository;
    private final RegistrationRepository registrationRepository;

    public CircuitController(CircuitRepository circuitRepository,
                             ViewingZoneRepository zoneRepository,
                             RegistrationRepository registrationRepository) {
        this.circuitRepository = circuitRepository;
        this.zoneRepository = zoneRepository;
        this.registrationRepository = registrationRepository;
    }

    @GetMapping
    public List<Circuit> getAllCircuits() {
        return circuitRepository.findAll();
    }

    @GetMapping("/{circuitId}/zones")
    public ResponseEntity<?> getZonesByCircuit(@PathVariable Integer circuitId) {
        List<ViewingZone> zones = zoneRepository.findByCircuitCircuitId(circuitId);
        var response = new ArrayList<>();

        for (ViewingZone z : zones) {
            long occupied = registrationRepository.countByViewingZoneZoneIdAndStatus(
                    z.getZoneId(), Registration.Status.confirmed);
            long remaining = Math.max(0, z.getTotalCapacity() - occupied);

            var item = new LinkedHashMap<>();
            item.put("zone_id", z.getZoneId());
            item.put("zone_name", z.getZoneName());
            item.put("zone_price", z.getZonePrice());
            item.put("total_capacity", z.getTotalCapacity());
            item.put("remaining_spots", remaining);
            item.put("available", remaining > 0);
            response.add(item);
        }
        return ResponseEntity.ok(response);
    }
}