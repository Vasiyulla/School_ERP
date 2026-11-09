package com.schoolerp.modules.operations.repository;

import com.schoolerp.modules.operations.entity.GatePass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GatePassRepository extends JpaRepository<GatePass, UUID> {
    List<GatePass> findAllByTenantId(String tenantId);
    List<GatePass> findAllByTenantIdAndPassStatus(String tenantId, String passStatus);
    List<GatePass> findAllByTenantIdAndStudentId(String tenantId, UUID studentId);
    Optional<GatePass> findByTenantIdAndQrToken(String tenantId, String qrToken);
    Optional<GatePass> findByTenantIdAndPassNumber(String tenantId, String passNumber);
    long countByTenantId(String tenantId);
}

