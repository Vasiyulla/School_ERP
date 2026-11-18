package com.schoolerp.modules.admissions.repository;

import com.schoolerp.modules.admissions.entity.AdmissionSeatSurrender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdmissionSeatSurrenderRepository extends JpaRepository<AdmissionSeatSurrender, UUID> {
    List<AdmissionSeatSurrender> findByTenantIdOrderByCreatedAtDesc(String tenantId);
    Optional<AdmissionSeatSurrender> findByTenantIdAndSurrenderRefNumber(String tenantId, String surrenderRefNumber);
    List<AdmissionSeatSurrender> findByTenantIdAndSettlementStatus(String tenantId, String settlementStatus);
}
