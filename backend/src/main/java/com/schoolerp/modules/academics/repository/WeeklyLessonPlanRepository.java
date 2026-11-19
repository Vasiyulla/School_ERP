package com.schoolerp.modules.academics.repository;

import com.schoolerp.modules.academics.entity.WeeklyLessonPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WeeklyLessonPlanRepository extends JpaRepository<WeeklyLessonPlan, UUID> {
    List<WeeklyLessonPlan> findAllByTenantIdAndCurriculumUnitId(String tenantId, UUID curriculumUnitId);
    List<WeeklyLessonPlan> findAllByTenantIdAndGradeLevelAndSubjectName(String tenantId, String gradeLevel, String subjectName);
}
