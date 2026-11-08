package com.schoolerp.modules.operations.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "transport_routes", schema = "operations")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransportRoute {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String routeName; // e.g. Route 7 - Andheri West to Campus

    private String vehicleRegistration;

    private String driverName;

    private String driverPhone;

    @Column(length = 2000)
    private String stopsList; // e.g. "Lokhandwala (06:45 AM) -> Infinity Mall (07:05 AM) -> DN Nagar (07:20 AM) -> Campus (07:45 AM)"

    private String morningStartTime; // 06:45 AM

    private String eveningStartTime; // 03:15 PM

    @Builder.Default
    private String status = "OPERATIONAL"; // OPERATIONAL, DELAYED, CANCELLED
}

