package com.schoolerp.modules.communication.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "ptm_slots", schema = "communication")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PtmSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private UUID sessionId;

    @Column(nullable = false)
    private UUID teacherId;

    @Column(nullable = false)
    private String teacherName; // "Dr. Vikram Seth"

    @Column(nullable = false)
    private String subject; // "Physics & Science"

    private String roomNumber; // "Lab 3 / Room 204"

    @Column(nullable = false)
    private LocalTime slotStartTime; // 09:00

    @Column(nullable = false)
    private LocalTime slotEndTime; // 09:15

    @Column(nullable = false)
    @Builder.Default
    private boolean booked = false;

    private UUID studentId;
    private String studentName;
    private String guardianName;
    private String guardianPhone;

    private LocalDateTime bookedAt;
}

