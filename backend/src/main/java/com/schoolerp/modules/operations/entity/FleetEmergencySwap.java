package com.schoolerp.modules.operations.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "fleet_emergency_swaps", schema = "operations", indexes = {
        @Index(name = "idx_fleet_swap_tenant", columnList = "tenant_id, status")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FleetEmergencySwap {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private String tenantId;

    @Column(name = "swap_reference", nullable = false, unique = true)
    private String swapReference;

    @Column(name = "broken_vehicle_reg", nullable = false)
    private String brokenVehicleReg;

    @Column(name = "replacement_vehicle_reg", nullable = false)
    private String replacementVehicleReg;

    @Column(name = "relief_driver_name", nullable = false)
    private String reliefDriverName;

    @Column(name = "relief_driver_phone", nullable = false)
    private String reliefDriverPhone;

    @Column(name = "route_code", nullable = false)
    private String routeCode;

    @Column(name = "breakdown_reason", length = 1000, nullable = false)
    private String breakdownReason;

    @Column(name = "affected_students_count", nullable = false)
    private int affectedStudentsCount;

    @Column(name = "parent_broadcast_dispatched", nullable = false)
    private boolean parentBroadcastDispatched;

    @Column(name = "status", nullable = false)
    private String status; // ACTIVE_SUBSTITUTION, RESOLVED

    @CreationTimestamp
    @Column(name = "incident_at", nullable = false, updatable = false)
    private Instant incidentAt;
}
