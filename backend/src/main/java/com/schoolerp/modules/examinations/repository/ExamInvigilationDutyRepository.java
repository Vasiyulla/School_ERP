package com.schoolerp.modules.examinations.repository;

import com.schoolerp.modules.examinations.entity.ExamInvigilationDuty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface ExamInvigilationDutyRepository extends JpaRepository<ExamInvigilationDuty, UUID> {
    List<ExamInvigilationDuty> findByExamDate(LocalDate examDate);
    List<ExamInvigilationDuty> findByInvigilatorStaffId(UUID invigilatorStaffId);
}
