package com.schoolerp.modules.operations.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "event_costume_rentals", schema = "operations")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventCostumeRental {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    private UUID eventId;

    @Column(nullable = false)
    private String eventName; // e.g. "Annual Cultural Day 2026"

    @Column(nullable = false)
    private String costumeCode; // e.g. "CST-DANCE-042"

    @Column(nullable = false)
    private String costumeDescription; // e.g. "Rajasthani Folk Dance Ghagra Set - Red/Gold"

    @Column(nullable = false)
    private String vendorSupplierName; // e.g. "Rangmanch Theatrical Costumers"

    private UUID assignedStudentId;
    private String assignedStudentName;

    @Builder.Default
    private BigDecimal rentalRatePerDay = BigDecimal.ZERO;

    private LocalDate issuedDate;
    private LocalDate expectedReturnDate;
    private LocalDate actualReturnDate;

    @Builder.Default
    private String returnCondition = "ISSUED_OUT"; // ISSUED_OUT, RETURNED_PRISTINE, RETURNED_DAMAGED, LOST

    @Builder.Default
    private BigDecimal damageFineAmount = BigDecimal.ZERO;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
