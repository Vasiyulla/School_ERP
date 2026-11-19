package com.schoolerp.modules.academics.repository;

import com.schoolerp.modules.academics.entity.CurriculumUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CurriculumUnitRepository extends JpaRepository<CurriculumUnit, UUID> {
    List<CurriculumUnit> findAllByTenantIdAndGradeLevelAndSubjectName(String tenantId, String gradeLevel, String subjectName);
    List<CurriculumUnit> findAllByTenantId(String tenantId);
    Optional<CurriculumUnit> findByIdAndTenantId(UUID id, String tenantId);
    long countByTenantId(String tenantId);
}
