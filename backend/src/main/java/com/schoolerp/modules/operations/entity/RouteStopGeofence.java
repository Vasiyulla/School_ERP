package com.schoolerp.modules.operations.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "route_stop_geofences", schema = "operations")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RouteStopGeofence {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private UUID routeId;

    @Column(nullable = false)
    private String stopName; // e.g. Lokhandwala Circle

    @Column(nullable = false)
    private int stopSequence; // 1, 2, 3...

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    @Builder.Default
    private double radiusMeters = 800.0; // 800 meter geofence threshold

    private String scheduledMorningTime; // 06:45 AM

    private String scheduledEveningTime; // 03:30 PM
}

