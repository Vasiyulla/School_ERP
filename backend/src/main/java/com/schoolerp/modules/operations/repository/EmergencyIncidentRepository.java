package com.schoolerp.modules.operations.repository;

import com.schoolerp.modules.operations.entity.EmergencyIncident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EmergencyIncidentRepository extends JpaRepository<EmergencyIncident, UUID> {
    List<EmergencyIncident> findByTenantIdOrderByCreatedAtDesc(String tenantId);
    List<EmergencyIncident> findByTenantIdAndStatusOrderByCreatedAtDesc(String tenantId, String status);
}

