package com.crud.demo.controller;

import com.crud.demo.model.*;
import com.crud.demo.repository.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/registrations")
@CrossOrigin(origins = "*")
public class RegistrationController {
    private final SpectatorRepository spectatorRepository;
    private final CircuitRepository circuitRepository;
    private final ViewingZoneRepository zoneRepository;
    private final RegistrationRepository registrationRepository;
    private final PaymentRepository paymentRepository;
    public RegistrationController(SpectatorRepository spectatorRepository, CircuitRepository circuitRepository, ViewingZoneRepository zoneRepository, RegistrationRepository registrationRepository, PaymentRepository paymentRepository) {
        this.spectatorRepository = spectatorRepository;
        this.circuitRepository = circuitRepository;
        this.zoneRepository = zoneRepository;
        this.registrationRepository = registrationRepository;
        this.paymentRepository = paymentRepository;
    }
    // HU-01 & HU-04: Registro de entrada, validación de cupos y registro de pago
    @PostMapping
    @Transactional
    public ResponseEntity<?> createBooking(@RequestBody Map<String, Object> req) {
        String fullName = req.get("full_name").toString();
        String typeDocument = req.get("type_document") != null ? req.get("type_document").toString() : "CC";
        String documentNumber = req.get("document_number").toString();
        String phone = req.get("phone") != null ? req.get("phone").toString() : null;
        String email = req.get("email").toString();
        Integer circuitId = Integer.parseInt(req.get("circuit_id").toString());
        Integer zoneId = Integer.parseInt(req.get("zone_id").toString());
        String paymentMethod = req.get("payment_method").toString();
        boolean paymentApproved = req.get("payment_approved") == null || Boolean.parseBoolean(req.get("payment_approved").toString());
        Circuit circuit = circuitRepository.findById(circuitId).orElse(null);
        ViewingZone zone = zoneRepository.findById(zoneId).orElse(null);
        if (circuit == null || zone == null || !zone.getCircuit().getCircuitId().equals(circuit.getCircuitId())) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid circuit or viewing zone."));
        }
        long occupied = registrationRepository.countByViewingZoneZoneIdAndStatus(zone.getZoneId(), Registration.Status.confirmed);
        if (occupied >= zone.getTotalCapacity()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "No spots remaining in this grandstand zone."));
        }
        Spectator spectator = spectatorRepository.findByDocumentNumber(documentNumber).orElseGet(Spectator::new);
        spectator.setFullName(fullName);
        spectator.setTypeDocument(typeDocument);
        spectator.setDocumentNumber(documentNumber);
        spectator.setPhone(phone);
        spectator.setEmail(email);
        spectator.setUpdatedAt(LocalDateTime.now());
        spectator = spectatorRepository.save(spectator);
        BigDecimal totalAmount = circuit.getBasePrice().add(zone.getZonePrice());
        Registration registration = new Registration();
        registration.setSpectator(spectator);
        registration.setCircuit(circuit);
        registration.setViewingZone(zone);
        registration.setTotalPaid(totalAmount);
        registration.setStatus(paymentApproved? Registration.Status.confirmed: Registration.Status.rejected);
        registration.setUpdatedAt(LocalDateTime.now());
        registration = registrationRepository.save(registration);
        Payment payment = new Payment();
        payment.setRegistration(registration);
        payment.setAmount(totalAmount);
        payment.setPaymentMethod(paymentMethod);
        payment.setTransactionReference("F1-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        payment.setPaymentStatus(paymentApproved? Payment.PaymentStatus.approved: Payment.PaymentStatus.rejected);
        payment.setIsRefundable(false);
        paymentRepository.save(payment);
        if (!paymentApproved) {
            return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED).body(Map.of("message", "Payment rejected. Registration voided and spot remains available.", "status", registration.getStatus()));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Registration confirmed!", "registration_id", registration.getRegistrationId(), "total_paid", totalAmount, "status", registration.getStatus()));
    }
    // HU-02: Consultar reserva activa por número de documento
    @GetMapping("/document/{documentNumber}")
    public ResponseEntity<?> getActiveBookingByDocument(@PathVariable String documentNumber) {
        List<Registration> list = registrationRepository.findBySpectatorDocumentNumberAndStatus(documentNumber, Registration.Status.confirmed);
        if (list.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "No active confirmed registration found for this document."));
        }
        Registration reg = list.get(0);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("registration_id", reg.getRegistrationId());
        data.put("spectator_id", reg.getSpectator().getSpectatorId());
        data.put("full_name", reg.getSpectator().getFullName());
        data.put("type_document", reg.getSpectator().getTypeDocument());
        data.put("document_number", reg.getSpectator().getDocumentNumber());
        data.put("phone", reg.getSpectator().getPhone());
        data.put("email", reg.getSpectator().getEmail());
        data.put("circuit_name", reg.getCircuit().getName());
        data.put("zone_name", reg.getViewingZone().getZoneName());
        data.put("total_paid", reg.getTotalPaid());
        return ResponseEntity.ok(data);
    }
    // HU-02: Actualizar datos personales antes de la carrera
    @PutMapping("/{registrationId}/spectator")
    @Transactional
    public ResponseEntity<?> updateSpectatorInfo(@PathVariable Integer registrationId, @RequestBody Map<String, Object> dto) {
        Registration reg = registrationRepository.findById(registrationId).orElse(null);
        if (reg == null || reg.getStatus() != Registration.Status.confirmed) {
            return ResponseEntity.notFound().build();
        }
        if (LocalDateTime.now().isAfter(reg.getCircuit().getRaceDate())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Race has already started; personal data can no longer be modified."));
        }
        Spectator spectator = reg.getSpectator();
        spectator.setFullName(dto.get("full_name").toString());
        if (dto.get("type_document") != null) {
            spectator.setTypeDocument(dto.get("type_document").toString());
        }
        spectator.setEmail(dto.get("email").toString());
        if (dto.get("phone") != null) {
            spectator.setPhone(dto.get("phone").toString());
        }
        spectator.setUpdatedAt(LocalDateTime.now());
        spectatorRepository.save(spectator);
        return ResponseEntity.ok(Map.of("message", "Personal details updated without altering zone or total paid."));
    }
    // HU-04: Cancelar asistencia
    @DeleteMapping("/{registrationId}")
    @Transactional
    public ResponseEntity<?> cancelAttendanceNoRefund(@PathVariable Integer registrationId) {
        Registration reg = registrationRepository.findById(registrationId).orElse(null);
        if (reg == null) {
            return ResponseEntity.notFound().build();
        }
        reg.setStatus(Registration.Status.cancelled);
        reg.setUpdatedAt(LocalDateTime.now());
        registrationRepository.save(reg);
        return ResponseEntity.ok(Map.of("message", "Registration cancelled under strict no-refund policy. Grandstand spot released.", "status", reg.getStatus()));
    }
    // HU-03: Panel del Organizador
    @GetMapping("/organizer/telemetry")
    public ResponseEntity<?> getOrganizerTelemetry(@RequestHeader(value = "X-Admin-Password", required = false) String password, HttpServletRequest request) {
        if (!"F1ADMIN2026".equals(password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid administrator password."));
        }
        List<ViewingZone> allZones = zoneRepository.findAll();
        var zonesReport = new ArrayList<>();
        long totalRegistered = 0;
        long totalRemaining = 0;
        BigDecimal grandTotalRevenue = BigDecimal.ZERO;
        for (ViewingZone z : allZones) {
            long confirmed = registrationRepository.countByViewingZoneZoneIdAndStatus(z.getZoneId(), Registration.Status.confirmed);
            long remaining = Math.max(0, z.getTotalCapacity() - confirmed);
            BigDecimal zoneRevenue = registrationRepository.sumRevenueByZone(z.getZoneId());
            totalRegistered += confirmed;
            totalRemaining += remaining;
            grandTotalRevenue = grandTotalRevenue.add(zoneRevenue);
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("circuit_name", z.getCircuit().getName());
            row.put("zone_name", z.getZoneName());
            row.put("total_capacity", z.getTotalCapacity());
            row.put("registered", confirmed);
            row.put("remaining_spots", remaining);
            row.put("zone_revenue", zoneRevenue);
            zonesReport.add(row);
        }
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("zones", zonesReport);
        result.put("total_registered", totalRegistered);
        result.put("total_remaining", totalRemaining);
        result.put("total_revenue", grandTotalRevenue);
        return ResponseEntity.ok(result);
    }
    // HU-03: Listado y filtrado de inscripciones para informes
    @GetMapping("/organizer/registrations")
    public ResponseEntity<?> getFilteredRegistrations(
            @RequestHeader(value = "X-Admin-Password", required = false) String password,
            @RequestParam(required = false) Integer circuitId,
            @RequestParam(required = false) String status,
            HttpServletRequest request) {
        var list = new ArrayList<>();
        for (Registration reg : registrationRepository.findAll()) {
            if (circuitId != null && !reg.getCircuit().getCircuitId().equals(circuitId)) {
                continue;
            }
            if (status != null && !status.isBlank() && !reg.getStatus().name().equals(status)) {
                continue;
            }
            var item = new LinkedHashMap<>();
            item.put("registration_id", reg.getRegistrationId());
            item.put("full_name", reg.getSpectator().getFullName());
            item.put("type_document", reg.getSpectator().getTypeDocument());
            item.put("document_number", reg.getSpectator().getDocumentNumber());
            item.put("email", reg.getSpectator().getEmail());
            item.put("phone", reg.getSpectator().getPhone());
            item.put("circuit_id", reg.getCircuit().getCircuitId());
            item.put("circuit_name", reg.getCircuit().getName());
            item.put("zone_name", reg.getViewingZone().getZoneName());
            item.put("total_paid", reg.getTotalPaid());
            item.put("status", reg.getStatus().name());
            item.put("registered_at", reg.getRegisteredAt());
            list.add(item);
        }
        return ResponseEntity.ok(list);
    }
}