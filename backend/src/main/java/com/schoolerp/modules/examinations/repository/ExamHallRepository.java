package com.schoolerp.modules.examinations.repository;

import com.schoolerp.modules.examinations.entity.ExamHall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ExamHallRepository extends JpaRepository<ExamHall, UUID> {
    List<ExamHall> findByTenantId(String tenantId);
}

