package com.schoolerp.modules.finance.repository;

import com.schoolerp.modules.finance.entity.FeeConcession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FeeConcessionRepository extends JpaRepository<FeeConcession, UUID> {
    List<FeeConcession> findByTenantId(String tenantId);
    List<FeeConcession> findByTenantIdAndStudentId(String tenantId, UUID studentId);
    Optional<FeeConcession> findByTenantIdAndStudentIdAndStatus(String tenantId, UUID studentId, String status);
}

