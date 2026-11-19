package com.schoolerp.modules.finance.repository;

import com.schoolerp.modules.finance.entity.ConcessionApprovalRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConcessionApprovalRequestRepository extends JpaRepository<ConcessionApprovalRequest, UUID> {
    List<ConcessionApprovalRequest> findAllByTenantIdAndStatus(String tenantId, String status);
    List<ConcessionApprovalRequest> findAllByTenantId(String tenantId);
    Optional<ConcessionApprovalRequest> findByIdAndTenantId(UUID id, String tenantId);
    long countByTenantId(String tenantId);
}
