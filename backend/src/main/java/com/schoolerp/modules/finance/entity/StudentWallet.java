package com.schoolerp.modules.finance.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "student_wallets", schema = "finance")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentWallet {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private UUID studentId;

    @Column(nullable = false)
    private String studentName;

    @Column(nullable = false)
    private String admissionNumber;

    @Column(nullable = false, unique = true)
    private String rfidCardNumber; // e.g. RFID-STU-884210

    @Column(nullable = false, precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(nullable = false, precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal dailySpendingLimit = new BigDecimal("200.00"); // Max INR 200/day

    @Column(nullable = false, precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal spentToday = BigDecimal.ZERO;

    private LocalDate lastSpentDate;

    @Column(nullable = false)
    @Builder.Default
    private String status = "ACTIVE"; // ACTIVE, BLOCKED, SUSPENDED

    private LocalDateTime lastTopupAt;

    @Version
    private Long version;
}

