package com.schoolerp.modules.operations.repository;

import com.schoolerp.modules.operations.entity.RouteStopGeofence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RouteStopGeofenceRepository extends JpaRepository<RouteStopGeofence, UUID> {
    List<RouteStopGeofence> findByTenantIdAndRouteIdOrderByStopSequenceAsc(String tenantId, UUID routeId);
    List<RouteStopGeofence> findByTenantId(String tenantId);
}

