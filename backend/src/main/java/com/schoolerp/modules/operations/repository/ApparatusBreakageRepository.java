package com.schoolerp.modules.operations.repository;

import com.schoolerp.modules.operations.entity.ApparatusBreakage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ApparatusBreakageRepository extends JpaRepository<ApparatusBreakage, UUID> {

    List<ApparatusBreakage> findAllByTenantIdOrderByCreatedAtDesc(String tenantId);

    List<ApparatusBreakage> findAllByTenantIdAndStudentId(String tenantId, UUID studentId);

    List<ApparatusBreakage> findAllByTenantIdAndLabName(String tenantId, String labName);

    Optional<ApparatusBreakage> findByIdAndTenantId(UUID id, String tenantId);
}

