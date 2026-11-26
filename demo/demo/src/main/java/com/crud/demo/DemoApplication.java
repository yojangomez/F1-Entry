package com.crud.demo;

import com.crud.demo.model.Circuit;
import com.crud.demo.model.ViewingZone;
import com.crud.demo.repository.CircuitRepository;
import com.crud.demo.repository.ViewingZoneRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    CommandLineRunner initDatabase(CircuitRepository circuitRepo, ViewingZoneRepository zoneRepo) {
        return args -> {
            if (circuitRepo.count() == 0) {
                Circuit monaco = circuitRepo.save(new Circuit(
                        "Circuit de Monaco", "Monte Carlo, Monaco",
                        LocalDateTime.of(2026, 12, 20, 14, 0), new BigDecimal("250.00")
                ));
                Circuit monza = circuitRepo.save(new Circuit(
                        "Autodromo Nazionale Monza", "Monza, Italy",
                        LocalDateTime.of(2026, 12, 28, 14, 0), new BigDecimal("180.00")
                ));
                Circuit spa = circuitRepo.save(new Circuit(
                        "Circuit de Spa-Francorchamps", "Stavelot, Belgium",
                        LocalDateTime.of(2026, 12, 30, 14, 0), new BigDecimal("200.00")
                ));

                zoneRepo.saveAll(List.of(
                        new ViewingZone(monaco, "Main Grandstand (Start/Finish)", new BigDecimal("200.00"), 500),
                        new ViewingZone(monaco, "Sainte-Dévote Corner", new BigDecimal("150.00"), 300),
                        new ViewingZone(monaco, "La Rascasse General Zone", new BigDecimal("90.00"), 200),

                        new ViewingZone(monza, "Tribuna Centrale", new BigDecimal("160.00"), 800),
                        new ViewingZone(monza, "Parabolica Curve", new BigDecimal("110.00"), 400),
                        new ViewingZone(monza, "Prato General Admission", new BigDecimal("60.00"), 500),

                        new ViewingZone(spa, "Eau Rouge / Raidillon Grandstand", new BigDecimal("180.00"), 600),
                        new ViewingZone(spa, "La Source Hairpin", new BigDecimal("130.00"), 350),
                        new ViewingZone(spa, "Bronze General Zone", new BigDecimal("70.00"), 450)
                ));
                System.out.println("Circuitos y zonas inicializados en f1entry.");
            }
        };
    }
}