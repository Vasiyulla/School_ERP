package com.schoolerp.modules.examinations.repository;

import com.schoolerp.modules.examinations.entity.InvigilationDuty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface InvigilationDutyRepository extends JpaRepository<InvigilationDuty, UUID> {
    List<InvigilationDuty> findByTenantIdAndExamDate(String tenantId, LocalDate examDate);
    List<InvigilationDuty> findByTenantIdAndTeacherId(String tenantId, UUID teacherId);
}

