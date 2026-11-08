package com.schoolerp.modules.operations.repository;

import com.schoolerp.modules.operations.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, UUID> {
    List<Vehicle> findByTenantId(String tenantId);
    Optional<Vehicle> findByTenantIdAndRegistrationNumber(String tenantId, String registrationNumber);
}

