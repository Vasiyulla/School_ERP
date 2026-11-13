package com.schoolerp.modules.admissions.repository;

import com.schoolerp.modules.admissions.entity.RteApplicationRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RteApplicationRepository extends JpaRepository<RteApplicationRecord, UUID> {
    List<RteApplicationRecord> findByTenantId(String tenantId);
    List<RteApplicationRecord> findByTenantIdAndAcademicYear(String tenantId, String academicYear);
    long countByTenantId(String tenantId);
}

