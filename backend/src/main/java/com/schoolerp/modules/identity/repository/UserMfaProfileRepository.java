package com.schoolerp.modules.identity.repository;

import com.schoolerp.modules.identity.entity.UserMfaProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserMfaProfileRepository extends JpaRepository<UserMfaProfile, UUID> {
    Optional<UserMfaProfile> findByUserIdAndTenantId(UUID userId, String tenantId);
    Optional<UserMfaProfile> findByEmailAndTenantId(String email, String tenantId);
    Optional<UserMfaProfile> findByEmail(String email);
}
