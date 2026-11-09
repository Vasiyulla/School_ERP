package com.schoolerp.modules.finance.repository;

import com.schoolerp.modules.finance.entity.FeeStructure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FeeStructureRepository extends JpaRepository<FeeStructure, UUID> {
    List<FeeStructure> findByTenantId(String tenantId);
    Optional<FeeStructure> findByTenantIdAndGradeLevelAndAcademicYear(String tenantId, String gradeLevel, String academicYear);
}

