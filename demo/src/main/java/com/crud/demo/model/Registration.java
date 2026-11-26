package com.crud.demo.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "registrations")
public class Registration {
    public enum Status {
        pending, confirmed, rejected, cancelled
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "registration_id")
    private Integer registrationId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "spectator_id", nullable = false)
    private Spectator spectator;

    @ManyToOne(optional = false)
    @JoinColumn(name = "circuit_id", nullable = false)
    private Circuit circuit;

    @ManyToOne(optional = false)
    @JoinColumn(name = "zone_id", nullable = false)
    private ViewingZone viewingZone;

    @Column(name = "total_paid", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPaid;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, columnDefinition = "ENUM('pending','confirmed','rejected','cancelled')")
    private Status status = Status.pending;

    @Column(name = "registered_at", updatable = false)
    private LocalDateTime registeredAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    public Registration() {}

    public Integer getRegistrationId() { 
        return registrationId; 
    }

    public void setRegistrationId(Integer registrationId) { 
        this.registrationId = registrationId; 
    }
    
    public Spectator getSpectator() { 
        return spectator; 
    }
    
    public void setSpectator(Spectator spectator) { 
        this.spectator = spectator; 
    }
    
    public Circuit getCircuit() { 
        return circuit; 
    }
    
    public void setCircuit(Circuit circuit) { 
        this.circuit = circuit; 
    }
    
    public ViewingZone getViewingZone() { 
        return viewingZone; 
    }
    
    public void setViewingZone(ViewingZone viewingZone) { 
        this.viewingZone = viewingZone; 
    }
    
    public BigDecimal getTotalPaid() { 
        return totalPaid; 
    }
    
    public void setTotalPaid(BigDecimal totalPaid) { 
        this.totalPaid = totalPaid; 
    }
    
    public Status getStatus() { 
        return status; 
    }
    
    public void setStatus(Status status) { 
        this.status = status; 
    }
    
    public LocalDateTime getRegisteredAt() { 
        return registeredAt; 
    }
    
    public void setRegisteredAt(LocalDateTime registeredAt) { 
        this.registeredAt = registeredAt; 
    }
    
    public LocalDateTime getUpdatedAt() { 
        return updatedAt; 
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) { 
        this.updatedAt = updatedAt; 
    }
}