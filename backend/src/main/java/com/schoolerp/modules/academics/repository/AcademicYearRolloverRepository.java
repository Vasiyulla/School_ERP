package com.schoolerp.modules.academics.repository;

import com.schoolerp.modules.academics.entity.AcademicYearRollover;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AcademicYearRolloverRepository extends JpaRepository<AcademicYearRollover, UUID> {
    List<AcademicYearRollover> findByTenantIdOrderByExecutedAtDesc(String tenantId);
}

