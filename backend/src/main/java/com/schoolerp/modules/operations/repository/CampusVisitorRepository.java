package com.schoolerp.modules.operations.repository;

import com.schoolerp.modules.operations.entity.CampusVisitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CampusVisitorRepository extends JpaRepository<CampusVisitor, UUID> {
    List<CampusVisitor> findByTenantIdAndStatus(String tenantId, String status);
    Optional<CampusVisitor> findByTenantIdAndVisitorPassNumber(String tenantId, String visitorPassNumber);
    Optional<CampusVisitor> findByTenantIdAndBadgeQrToken(String tenantId, String badgeQrToken);
}

