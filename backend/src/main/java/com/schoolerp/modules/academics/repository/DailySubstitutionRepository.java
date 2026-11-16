package com.schoolerp.modules.academics.repository;

import com.schoolerp.modules.academics.entity.DailySubstitution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface DailySubstitutionRepository extends JpaRepository<DailySubstitution, UUID> {
    List<DailySubstitution> findBySubstitutionDate(LocalDate substitutionDate);
    List<DailySubstitution> findByTenantIdAndSubstitutionDate(String tenantId, LocalDate substitutionDate);
    List<DailySubstitution> findBySubstituteTeacherId(UUID substituteTeacherId);
}
