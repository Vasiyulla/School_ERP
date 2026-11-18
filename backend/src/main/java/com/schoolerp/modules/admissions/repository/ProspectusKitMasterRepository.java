package com.schoolerp.modules.admissions.repository;

import com.schoolerp.modules.admissions.entity.ProspectusKitMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProspectusKitMasterRepository extends JpaRepository<ProspectusKitMaster, UUID> {
    List<ProspectusKitMaster> findByTenantIdOrderByCreatedAtDesc(String tenantId);
    Optional<ProspectusKitMaster> findByTenantIdAndSerialBarcode(String tenantId, String serialBarcode);
    long countByTenantIdAndStatus(String tenantId, String status);
    List<ProspectusKitMaster> findByTenantIdAndStatus(String tenantId, String status);
}
