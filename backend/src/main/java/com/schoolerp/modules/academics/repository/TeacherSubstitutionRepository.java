package com.schoolerp.modules.academics.repository;

import com.schoolerp.modules.academics.entity.TeacherSubstitution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface TeacherSubstitutionRepository extends JpaRepository<TeacherSubstitution, UUID> {
    List<TeacherSubstitution> findByTenantIdAndSubstitutionDateOrderByPeriodNumberAsc(String tenantId, LocalDate substitutionDate);
    List<TeacherSubstitution> findByTenantIdAndSubstituteTeacherIdAndSubstitutionDate(String tenantId, UUID substituteTeacherId, LocalDate substitutionDate);
}

