package com.crud.demo.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "viewing_zones")
public class ViewingZone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "zone_id")
    private Integer zoneId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "circuit_id", nullable = false)
    private Circuit circuit;

    @Column(name = "zone_name", nullable = false, length = 100)
    private String zoneName;

    @Column(name = "zone_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal zonePrice;

    @Column(name = "total_capacity", nullable = false)
    private Integer totalCapacity;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public ViewingZone() {}

    public ViewingZone(Circuit circuit, String zoneName, BigDecimal zonePrice, Integer totalCapacity) {
        this.circuit = circuit;
        this.zoneName = zoneName;
        this.zonePrice = zonePrice;
        this.totalCapacity = totalCapacity;
    }

    public Integer getZoneId() { 
        return zoneId; 
    }
    
    public void setZoneId(Integer zoneId) { 
        this.zoneId = zoneId; 
    }
    
    public Circuit getCircuit() { 
        return circuit; 
    }
    
    public void setCircuit(Circuit circuit) { 
        this.circuit = circuit; 
    }
    
    public String getZoneName() { 
        return zoneName; 
    }
    
    public void setZoneName(String zoneName) { 
        this.zoneName = zoneName; 
    }
    
    public BigDecimal getZonePrice() { 
        return zonePrice; 
    }
    
    public void setZonePrice(BigDecimal zonePrice) { 
        this.zonePrice = zonePrice; 
    }
    
    public Integer getTotalCapacity() { 
        return totalCapacity; 
    }
    
    public void setTotalCapacity(Integer totalCapacity) { 
        this.totalCapacity = totalCapacity; 
    }
    
    public LocalDateTime getCreatedAt() { 
        return createdAt; 
    }
    
    public void setCreatedAt(LocalDateTime createdAt) { 
        this.createdAt = createdAt; 
    }
}