package com.schoolerp.modules.students.repository;

import com.schoolerp.modules.students.entity.BehaviorIncident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BehaviorIncidentRepository extends JpaRepository<BehaviorIncident, UUID> {
    List<BehaviorIncident> findAllByTenantIdOrderByRecordedAtDesc(String tenantId);
    List<BehaviorIncident> findAllByTenantIdAndStudentIdOrderByRecordedAtDesc(String tenantId, UUID studentId);
    List<BehaviorIncident> findAllByTenantIdAndHouseNameOrderByRecordedAtDesc(String tenantId, String houseName);
}

