package com.schoolerp.modules.academics.repository;

import com.schoolerp.modules.academics.entity.AdaptiveStudyPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AdaptiveStudyPlanRepository extends JpaRepository<AdaptiveStudyPlan, UUID> {
    List<AdaptiveStudyPlan> findByStudentIdOrderByAssignedDateDesc(UUID studentId);
    List<AdaptiveStudyPlan> findByTenantIdAndClassNameAndSubjectName(String tenantId, String className, String subjectName);
}

