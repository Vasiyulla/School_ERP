package com.schoolerp.modules.operations.repository;

import com.schoolerp.modules.operations.entity.GpsPing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GpsPingRepository extends JpaRepository<GpsPing, UUID> {
    List<GpsPing> findTop10ByTenantIdAndVehicleRegistrationOrderByRecordedAtDesc(String tenantId, String vehicleRegistration);
    Optional<GpsPing> findTop1ByTenantIdAndVehicleRegistrationOrderByRecordedAtDesc(String tenantId, String vehicleRegistration);
}

