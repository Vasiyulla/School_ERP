package com.schoolerp.modules.finance.repository;

import com.schoolerp.modules.finance.entity.SiblingConcessionPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SiblingConcessionPolicyRepository extends JpaRepository<SiblingConcessionPolicy, UUID> {
    List<SiblingConcessionPolicy> findAllByTenantId(String tenantId);
    Optional<SiblingConcessionPolicy> findByTenantIdAndPolicyCode(String tenantId, String policyCode);
    Optional<SiblingConcessionPolicy> findByTenantIdAndSiblingRankAndIsActiveTrue(String tenantId, Integer siblingRank);
}

