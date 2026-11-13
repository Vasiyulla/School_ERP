package com.schoolerp.modules.communication.repository;

import com.schoolerp.modules.communication.entity.GrievanceTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GrievanceTicketRepository extends JpaRepository<GrievanceTicket, UUID> {

    List<GrievanceTicket> findAllByTenantIdOrderByCreatedAtDesc(String tenantId);

    List<GrievanceTicket> findAllByTenantIdAndStatusOrderByCreatedAtDesc(String tenantId, String status);

    List<GrievanceTicket> findAllByTenantIdAndCurrentTierOrderByCreatedAtDesc(String tenantId, String currentTier);

    Optional<GrievanceTicket> findByIdAndTenantId(UUID id, String tenantId);

    long countByTenantId(String tenantId);
}

