package com.schoolerp.modules.admissions.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "admission_point_criteria", schema = "admissions",
       uniqueConstraints = @UniqueConstraint(name = "uq_tenant_inquiry_criteria", columnNames = {"tenantId", "inquiryId"}))
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdmissionPointCriteria {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private UUID inquiryId;

    @Builder.Default
    private BigDecimal distanceKm = BigDecimal.ZERO;

    @Builder.Default
    private int distancePoints = 0; // Max 40 points

    @Builder.Default
    private boolean hasSibling = false;

    @Builder.Default
    private int siblingPoints = 0; // Max 30 points

    private UUID siblingStudentId;

    @Builder.Default
    private boolean hasAlumniParent = false;

    @Builder.Default
    private int alumniPoints = 0; // Max 10 points

    private Integer alumniPassingYear;

    @Builder.Default
    private boolean staffWard = false;

    @Builder.Default
    private int staffWardPoints = 0; // Max 10 points

    private UUID staffEmployeeId;

    @Builder.Default
    private boolean singleGirlChild = false;

    @Builder.Default
    private int girlChildPoints = 0; // Max 5 points

    @Builder.Default
    private boolean firstBorn = false;

    @Builder.Default
    private int firstBornPoints = 0; // Max 5 points

    @Builder.Default
    private int totalCalculatedPoints = 0; // Out of 100

    private Integer tieBreakerDrawNumber;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
}
