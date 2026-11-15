package com.schoolerp.modules.admissions.repository;

import com.schoolerp.modules.admissions.entity.GradeSeatCapacity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GradeSeatCapacityRepository extends JpaRepository<GradeSeatCapacity, UUID> {
    Optional<GradeSeatCapacity> findByTenantIdAndAcademicYearAndGradeName(String tenantId, String academicYear, String gradeName);
    List<GradeSeatCapacity> findByTenantIdAndAcademicYear(String tenantId, String academicYear);
    List<GradeSeatCapacity> findByTenantId(String tenantId);
    Optional<GradeSeatCapacity> findByIdAndTenantId(UUID id, String tenantId);
}

