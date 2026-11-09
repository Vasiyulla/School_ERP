package com.schoolerp.modules.communication.repository;

import com.schoolerp.modules.communication.entity.CircularAcknowledgment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CircularAcknowledgmentRepository extends JpaRepository<CircularAcknowledgment, UUID> {
    List<CircularAcknowledgment> findByTenantIdAndCircularId(String tenantId, UUID circularId);
    Optional<CircularAcknowledgment> findByTenantIdAndCircularIdAndGuardianEmail(String tenantId, UUID circularId, String guardianEmail);
    List<CircularAcknowledgment> findByTenantIdAndGuardianEmail(String tenantId, String guardianEmail);
}

