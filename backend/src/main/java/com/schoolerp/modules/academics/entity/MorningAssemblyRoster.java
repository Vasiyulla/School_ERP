package com.schoolerp.modules.academics.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "morning_assemblies", schema = "academics")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MorningAssemblyRoster {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private LocalDate weekStartDate;

    @Column(nullable = false)
    private String houseOnDuty; // Gandhi House (Red), Tagore House (Blue), Shivaji House (Green), Raman House (Yellow)

    @Column(nullable = false)
    private String newsReaderStudentName;

    @Column(nullable = false, length = 1000)
    private String thoughtForTheDay;

    @Column(nullable = false)
    private String thoughtPresenterStudentName;

    @Column(nullable = false)
    private String pledgeLeaderStudentName;

    @Column(nullable = false)
    private String nationalAnthemLeadStudentName;

    @Column(nullable = false)
    private String teacherInChargeName;

    @Column(nullable = false)
    @Builder.Default
    private String status = "ACTIVE"; // ACTIVE, COMPLETED

    @Builder.Default
    private Instant createdAt = Instant.now();
}

