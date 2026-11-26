package com.crud.demo.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "circuits")
public class Circuit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "circuit_id")
    private Integer circuitId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "location", nullable = false, length = 100)
    private String location;

    @Column(name = "race_date", nullable = false)
    private LocalDateTime raceDate;

    @Column(name = "base_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal basePrice;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Circuit() {}

    public Circuit(String name, String location, LocalDateTime raceDate, BigDecimal basePrice) {
        this.name = name;
        this.location = location;
        this.raceDate = raceDate;
        this.basePrice = basePrice;
    }

    public Integer getCircuitId() { return circuitId; }

    public void setCircuitId(Integer circuitId) { this.circuitId = circuitId; }
    
    public String getName() { return name; }
    
    public void setName(String name) { this.name = name; }
    
    public String getLocation() { return location; }
    
    public void setLocation(String location) { this.location = location; }
    
    public LocalDateTime getRaceDate() { return raceDate; }
    
    public void setRaceDate(LocalDateTime raceDate) { this.raceDate = raceDate; }
    
    public BigDecimal getBasePrice() { return basePrice; }
    
    public void setBasePrice(BigDecimal basePrice) { this.basePrice = basePrice; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}