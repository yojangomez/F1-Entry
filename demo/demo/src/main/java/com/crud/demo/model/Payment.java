package com.crud.demo.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment {
    public enum PaymentStatus {
        pending, approved, rejected
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Integer paymentId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "registration_id", nullable = false)
    private Registration registration;

    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "payment_method", nullable = false, length = 50)
    private String paymentMethod;

    @Column(name = "transaction_reference", unique = true, length = 100)
    private String transactionReference;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false, columnDefinition = "ENUM('pending','approved','rejected')")
    private PaymentStatus paymentStatus = PaymentStatus.pending;

    @Column(name = "is_refundable", nullable = false)
    private Boolean isRefundable = false;

    @Column(name = "payment_date", updatable = false)
    private LocalDateTime paymentDate = LocalDateTime.now();

    public Payment() {}

    public Integer getPaymentId() { 
        return paymentId; 
    }
    
    public void setPaymentId(Integer paymentId) { 
        this.paymentId = paymentId; 
    }
    
    public Registration getRegistration() { 
        return registration; 
    }
    
    public void setRegistration(Registration registration) { 
        this.registration = registration; 
    }
    
    public BigDecimal getAmount() { 
        return amount; 
    }
    
    public void setAmount(BigDecimal amount) { 
        this.amount = amount; 
    }
    
    public String getPaymentMethod() { 
        return paymentMethod; 
    }
    
    public void setPaymentMethod(String paymentMethod) { 
        this.paymentMethod = paymentMethod; 
    }
    
    public String getTransactionReference() { 
        return transactionReference; 
    }
    
    public void setTransactionReference(String transactionReference) { 
        this.transactionReference = transactionReference; 
    }
    
    public PaymentStatus getPaymentStatus() { 
        return paymentStatus; 
    }
    
    public void setPaymentStatus(PaymentStatus paymentStatus) { 
        this.paymentStatus = paymentStatus; 
    }
    
    public Boolean getIsRefundable() { 
        return isRefundable; 
    }
    
    public void setIsRefundable(Boolean isRefundable) { 
        this.isRefundable = isRefundable; 
    }
    
    public LocalDateTime getPaymentDate() { 
        return paymentDate; 
    }
    
    public void setPaymentDate(LocalDateTime paymentDate) { 
        this.paymentDate = paymentDate; 
    }
}