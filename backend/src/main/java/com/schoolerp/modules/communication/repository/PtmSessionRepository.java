package com.schoolerp.modules.communication.repository;

import com.schoolerp.modules.communication.entity.PtmSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PtmSessionRepository extends JpaRepository<PtmSession, UUID> {
    List<PtmSession> findAllByTenantId(String tenantId);
    List<PtmSession> findAllByTenantIdAndStatus(String tenantId, String status);
}

