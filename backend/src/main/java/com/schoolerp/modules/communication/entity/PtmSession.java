package com.schoolerp.modules.communication.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "ptm_sessions", schema = "communication")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PtmSession {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String sessionTitle; // "Term 1 Parent-Teacher Academic Conference"

    @Column(nullable = false)
    private LocalDate sessionDate;

    @Column(nullable = false)
    private LocalTime startTime; // 09:00

    @Column(nullable = false)
    private LocalTime endTime; // 13:00

    @Column(nullable = false)
    @Builder.Default
    private int slotDurationMinutes = 15; // 15-minute conference blocks

    @Column(nullable = false)
    @Builder.Default
    private String status = "OPEN_FOR_BOOKING"; // OPEN_FOR_BOOKING, CLOSED, COMPLETED
}

