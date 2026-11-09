package com.schoolerp.modules.communication.repository;

import com.schoolerp.modules.communication.entity.DeviceToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeviceTokenRepository extends JpaRepository<DeviceToken, UUID> {
    Optional<DeviceToken> findByTenantIdAndDeviceToken(String tenantId, String deviceToken);
    List<DeviceToken> findByTenantIdAndUserIdAndStatus(String tenantId, UUID userId, String status);
    List<DeviceToken> findByTenantIdAndUserRoleAndStatus(String tenantId, String userRole, String status);
    List<DeviceToken> findByTenantId(String tenantId);
}

