package com.schoolerp.modules.institution.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "academic_calendar_events", schema = "institution")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AcademicCalendarEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String eventTitle;

    @Column(nullable = false)
    private String eventType; // HOLIDAY, VACATION, EXAM_WEEK, PTM_DAY, RAINY_DAY_CLOSURE, HALF_DAY_BELL

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Builder.Default
    private boolean workingDay = false;

    @Column(length = 500)
    private String bellSchedulePreset; // STANDARD_8_PERIODS, WINTER_TIMING, HALF_DAY_4_PERIODS, CLOSED

    @Column(length = 1000)
    private String description;

    @Builder.Default
    private Instant createdAt = Instant.now();
}

