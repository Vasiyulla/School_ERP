package com.schoolerp.modules.academics.repository;

import com.schoolerp.modules.academics.entity.EventParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EventParticipantRepository extends JpaRepository<EventParticipant, UUID> {

    List<EventParticipant> findAllByTenantIdAndEventIdOrderByStudentNameAsc(String tenantId, UUID eventId);

    List<EventParticipant> findAllByTenantIdAndStudentId(String tenantId, UUID studentId);

    Optional<EventParticipant> findByTenantIdAndEventIdAndStudentId(String tenantId, UUID eventId, UUID studentId);

    Optional<EventParticipant> findByIdAndTenantId(UUID id, String tenantId);

    long countByTenantIdAndEventId(String tenantId, UUID eventId);
}

