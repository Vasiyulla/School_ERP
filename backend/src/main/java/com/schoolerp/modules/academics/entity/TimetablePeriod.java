package com.schoolerp.modules.academics.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "timetable_periods", schema = "academics")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimetablePeriod {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String dayOfWeek; // Monday, Tuesday, Wednesday, Thursday, Friday

    @Column(nullable = false)
    private int periodNumber; // 1 to 7

    @Column(nullable = false)
    private String timeSlot; // 08:30 – 09:15

    @Column(nullable = false)
    private String subject; // Mathematics, Physics, Chemistry

    private String className;
    private String sectionName;
    private String teacherName;
    private String roomNumber;
    private boolean isLab;
    private boolean isFree;
}

