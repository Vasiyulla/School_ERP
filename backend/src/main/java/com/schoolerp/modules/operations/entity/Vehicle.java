package com.schoolerp.modules.operations.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "vehicles", schema = "operations")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false, unique = true)
    private String registrationNumber; // e.g. MH-02-CB-4012

    private String vehicleModel; // e.g. Tata Starbus 42-Seater

    private int seatingCapacity;

    private String driverName;

    private String driverPhone;

    private String gpsDeviceId; // IoT Hardware ID e.g. GPS-TK103-994

    @Builder.Default
    private String status = "ACTIVE"; // ACTIVE, MAINTENANCE, RETIRED
}

