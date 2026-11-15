package com.schoolerp.modules.operations.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "hostel_roll_calls", schema = "operations")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HostelRollCall {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private UUID allocationId;

    @Column(nullable = false)
    private UUID studentId;

    @Column(nullable = false)
    private LocalDate rollCallDate;

    @Column(nullable = false)
    private String sessionType; // MORNING_WAKEUP, NIGHT_LIGHTS_OUT

    @Builder.Default
    private String status = "PRESENT"; // PRESENT, ABSENT, ON_EXEAT, INFIRMARY

    @Column(columnDefinition = "TEXT")
    private String remarks;

    @Column(nullable = false)
    private String wardenName;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}

