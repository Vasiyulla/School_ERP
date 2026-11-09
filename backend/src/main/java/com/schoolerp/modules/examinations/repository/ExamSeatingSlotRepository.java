package com.schoolerp.modules.examinations.repository;

import com.schoolerp.modules.examinations.entity.ExamSeatingSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface ExamSeatingSlotRepository extends JpaRepository<ExamSeatingSlot, UUID> {
    List<ExamSeatingSlot> findByTenantIdAndHallIdAndExamDate(String tenantId, UUID hallId, LocalDate examDate);
    List<ExamSeatingSlot> findByTenantIdAndExamDate(String tenantId, LocalDate examDate);
}

