package com.schoolerp.modules.admissions.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "grade_seat_capacities", schema = "admissions",
       uniqueConstraints = @UniqueConstraint(name = "uq_tenant_year_grade", columnNames = {"tenantId", "academicYear", "gradeName"}))
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GradeSeatCapacity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String academicYear; // e.g. "2026-2027"

    @Column(nullable = false)
    private String gradeName; // e.g. "Grade 1", "Grade 6"

    @Column(nullable = false)
    private int totalSanctionedSeats; // e.g. 140

    @Builder.Default
    private int generalQuotaSeats = 0; // 60%

    @Builder.Default
    private int rteQuotaSeats = 0; // 25%

    @Builder.Default
    private int siblingStaffQuotaSeats = 0; // 10%

    @Builder.Default
    private int managementQuotaSeats = 0; // 5%

    @Builder.Default
    private int obcQuotaSeats = 0;

    @Builder.Default
    private int scQuotaSeats = 0;

    @Builder.Default
    private int stQuotaSeats = 0;

    @Builder.Default
    private int cwsnQuotaSeats = 0;

    @Builder.Default
    private int staffWardQuotaSeats = 0;

    @Builder.Default
    private int singleGirlChildQuotaSeats = 0;

    @Builder.Default
    private int enrolledCount = 0;

    @Builder.Default
    private int activeOffersCount = 0;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
}

