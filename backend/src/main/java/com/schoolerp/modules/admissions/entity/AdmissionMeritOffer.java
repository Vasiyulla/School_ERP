package com.schoolerp.modules.admissions.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "admission_merit_offers", schema = "admissions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdmissionMeritOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private UUID inquiryId;

    @Column(nullable = false)
    private String offerNumber; // e.g. "OFFER-2026-0042"

    @Column(nullable = false)
    private String studentName;

    @Column(nullable = false)
    private String gradeOffered;

    @Column(nullable = false)
    private String academicYear;

    @Builder.Default
    private BigDecimal tuitionFeeTotal = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal scholarshipOrConcessionAmount = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal netAdmissionPayable = BigDecimal.ZERO;

    private LocalDate validUntilDate;

    @Builder.Default
    private BigDecimal seatLockDepositAmount = BigDecimal.ZERO;

    private String parentSignatureName;

    private String parentIpAddress;

    private String depositTransactionId;

    @Builder.Default
    private String depositStatus = "PENDING"; // PENDING, PAID, LAPSED

    @Builder.Default
    private String offerStatus = "ISSUED"; // ISSUED, ACCEPTED, DECLINED, EXPIRED, CONVERTED_TO_STUDENT

    private UUID convertedStudentId;

    private UUID convertedInvoiceId;

    @Builder.Default
    private LocalDateTime issuedAt = LocalDateTime.now();

    private LocalDateTime acceptedAt;
}

