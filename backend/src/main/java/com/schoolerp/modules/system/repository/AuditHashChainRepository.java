package com.schoolerp.modules.system.repository;

import com.schoolerp.modules.system.entity.AuditHashChain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AuditHashChainRepository extends JpaRepository<AuditHashChain, UUID> {
    Optional<AuditHashChain> findTopByTenantIdOrderByChainIndexDesc(String tenantId);
    List<AuditHashChain> findAllByTenantIdOrderByChainIndexAsc(String tenantId);
    List<AuditHashChain> findAllByTenantIdOrderByChainIndexDesc(String tenantId);
}
