package com.schoolerp.modules.operations.repository;

import com.schoolerp.modules.operations.entity.HostelExeatPass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface HostelExeatPassRepository extends JpaRepository<HostelExeatPass, UUID> {
    List<HostelExeatPass> findByTenantIdAndStudentId(String tenantId, UUID studentId);
    List<HostelExeatPass> findByTenantIdAndStatus(String tenantId, String status);
    Optional<HostelExeatPass> findByTenantIdAndPassNumber(String tenantId, String passNumber);
}

