package com.schoolerp.modules.staff.repository;

import com.schoolerp.modules.staff.entity.CpcPayMatrix;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CpcPayMatrixRepository extends JpaRepository<CpcPayMatrix, UUID> {
    List<CpcPayMatrix> findByTenantId(String tenantId);
    Optional<CpcPayMatrix> findByTenantIdAndPayLevel(String tenantId, String payLevel);
}

