package com.schoolerp.modules.students.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "higher_ed_placements", schema = "students")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HigherEdPlacement {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private UUID alumniProfileId;

    @Column(nullable = false)
    private String studentName;

    @Column(nullable = false)
    private String universityName; // e.g. "IIT Bombay", "AIIMS New Delhi", "Stanford University"

    @Column(nullable = false)
    @Builder.Default
    private String country = "India";

    @Column(nullable = false)
    private String degreeProgram; // e.g. "B.Tech Computer Science", "MBBS"

    @Column(nullable = false)
    @Builder.Default
    private String admissionType = "ENTRANCE_RANK"; // DIRECT_MERIT, ENTRANCE_RANK, SCHOLARSHIP

    private String entranceExamName; // e.g. JEE Advanced, NEET, CUET, SAT

    private String entranceScoreOrRank; // e.g. "AIR 142", "99.8 Percentile", "1540/1600"

    @Builder.Default
    private BigDecimal scholarshipAmountInr = BigDecimal.ZERO;

    @Builder.Default
    private boolean verifiedBySchool = true;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}

