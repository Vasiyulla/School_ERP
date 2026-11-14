package com.schoolerp.modules.academics.repository;

import com.schoolerp.modules.academics.entity.ExitTicketSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ExitTicketSessionRepository extends JpaRepository<ExitTicketSession, UUID> {
    Optional<ExitTicketSession> findBySessionPinAndStatus(String sessionPin, String status);
    List<ExitTicketSession> findByTenantIdAndClassNameAndSectionNameOrderByCreatedAtDesc(String tenantId, String className, String sectionName);
}

