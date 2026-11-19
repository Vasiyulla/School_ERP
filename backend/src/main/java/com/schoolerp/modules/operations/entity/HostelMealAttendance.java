package com.schoolerp.modules.operations.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "hostel_meal_attendances", schema = "operations", indexes = {
        @Index(name = "idx_meal_punch_lookup", columnList = "tenant_id, student_id, meal_date, meal_type")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HostelMealAttendance {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private String tenantId;

    @Column(name = "student_id", nullable = false)
    private UUID studentId;

    @Column(name = "student_name", nullable = false)
    private String studentName;

    @Column(name = "rfid_card_uid", nullable = false)
    private String rfidCardUid;

    @Column(name = "meal_date", nullable = false)
    private LocalDate mealDate;

    @Column(name = "meal_type", nullable = false)
    private String mealType; // BREAKFAST, LUNCH, EVENING_SNACK, DINNER

    @Column(name = "dining_hall_gate", nullable = false)
    private String diningHallGate;

    @CreationTimestamp
    @Column(name = "punched_at", nullable = false, updatable = false)
    private Instant punchedAt;
}
