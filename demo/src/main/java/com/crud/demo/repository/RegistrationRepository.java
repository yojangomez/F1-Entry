package com.crud.demo.repository;

import com.crud.demo.model.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.util.List;

public interface RegistrationRepository extends JpaRepository<Registration, Integer> {

    long countByViewingZoneZoneIdAndStatus(Integer zoneId, Registration.Status status);

    List<Registration> findBySpectatorDocumentNumberAndStatus(String documentNumber, Registration.Status status);

    // Suma únicamente el dinero real recaudado de las entradas confirmadas
    @Query("SELECT COALESCE(SUM(r.totalPaid), 0) FROM Registration r WHERE r.viewingZone.zoneId = :zoneId AND r.status = 'confirmed'")
    BigDecimal sumRevenueByZone(@Param("zoneId") Integer zoneId);
}