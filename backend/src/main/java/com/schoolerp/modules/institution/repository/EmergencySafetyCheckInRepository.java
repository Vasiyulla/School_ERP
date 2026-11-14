package com.schoolerp.modules.institution.repository;

import com.schoolerp.modules.institution.entity.EmergencySafetyCheckIn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EmergencySafetyCheckInRepository extends JpaRepository<EmergencySafetyCheckIn, UUID> {
    List<EmergencySafetyCheckIn> findByTenantIdOrderByReportedAtDesc(String tenantId);
    List<EmergencySafetyCheckIn> findByTenantIdAndSafetyStatus(String tenantId, String safetyStatus);
}

