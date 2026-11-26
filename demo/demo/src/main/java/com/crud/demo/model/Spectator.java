package com.crud.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "spectators")
public class Spectator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "spectator_id")
    private Integer spectatorId;

    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @Column(name = "type_document", nullable = false, length = 100)
    private String typeDocument;

    @Column(name = "document_number", nullable = false, unique = true, length = 50)
    private String documentNumber;

    @Column(name = "phone", length = 30)
    private String phone;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    public Spectator() {}

    public Integer getSpectatorId() { 
        return spectatorId; 
    }

    public void setSpectatorId(Integer spectatorId) { 
        this.spectatorId = spectatorId; 
    }
    
    public String getFullName() { 
        return fullName; 
    }
    
    public void setFullName(String fullName) { 
        this.fullName = fullName; 
    }
    
    public String getTypeDocument() { 
        return typeDocument; 
    }
    
    public void setTypeDocument(String typeDocument) { 
        this.typeDocument = typeDocument; 
    }
    
    public String getDocumentNumber() { 
        return documentNumber; 
    }
    
    public void setDocumentNumber(String documentNumber) { 
        this.documentNumber = documentNumber; 
    }
    
    public String getPhone() { 
        return phone; 
    }
    
    public void setPhone(String phone) { 
        this.phone = phone; 
    }
    
    public String getEmail() { 
        return email; 
    }
    
    public void setEmail(String email) { 
        this.email = email; 
    }
    
    public LocalDateTime getCreatedAt() { 
        return createdAt; 
    }
    
    public void setCreatedAt(LocalDateTime createdAt) { 
        this.createdAt = createdAt; 
    }
    
    public LocalDateTime getUpdatedAt() { 
        return updatedAt; 
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) { 
        this.updatedAt = updatedAt; 
    }
}