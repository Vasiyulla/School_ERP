package com.schoolerp.modules.operations.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "hostel_room_inspections", schema = "operations", indexes = {
        @Index(name = "idx_room_inspection_student", columnList = "tenant_id, student_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HostelRoomInspection {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private String tenantId;

    @Column(name = "student_id", nullable = false)
    private UUID studentId;

    @Column(name = "student_name", nullable = false)
    private String studentName;

    @Column(name = "room_number", nullable = false)
    private String roomNumber;

    @Column(name = "inspection_type", nullable = false)
    private String inspectionType; // MOVE_IN, TERM_END_CHECKOUT

    @Column(name = "bed_frame_status", nullable = false)
    private String bedFrameStatus; // INTACT, DAMAGED

    @Column(name = "mattress_status", nullable = false)
    private String mattressStatus; // INTACT, STAINED_DAMAGED

    @Column(name = "study_table_status", nullable = false)
    private String studyTableStatus; // INTACT, DEFACED

    @Column(name = "wardrobe_status", nullable = false)
    private String wardrobeStatus; // INTACT, LOCK_BROKEN

    @Column(name = "electrical_fixtures_status", nullable = false)
    private String electricalFixturesStatus; // INTACT, DAMAGED

    @Column(name = "assessed_damage_fee", precision = 12, scale = 2, nullable = false)
    private BigDecimal assessedDamageFee;

    @Column(name = "caution_money_deduction_applied", nullable = false)
    private boolean cautionMoneyDeductionApplied;

    @Column(name = "inspector_remarks", length = 1000)
    private String inspectorRemarks;

    @CreationTimestamp
    @Column(name = "inspected_at", nullable = false, updatable = false)
    private Instant inspectedAt;
}
