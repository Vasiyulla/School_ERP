package com.schoolerp.modules.academics.repository;

import com.schoolerp.modules.academics.entity.StudentRiskAssessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StudentRiskAssessmentRepository extends JpaRepository<StudentRiskAssessment, UUID> {
    List<StudentRiskAssessment> findByTenantIdOrderByCompositeRiskScoreDesc(String tenantId);
    List<StudentRiskAssessment> findByTenantIdAndRiskTierInOrderByCompositeRiskScoreDesc(String tenantId, List<String> tiers);
}

