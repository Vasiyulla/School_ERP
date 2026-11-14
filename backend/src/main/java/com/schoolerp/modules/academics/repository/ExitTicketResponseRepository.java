package com.schoolerp.modules.academics.repository;

import com.schoolerp.modules.academics.entity.ExitTicketResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ExitTicketResponseRepository extends JpaRepository<ExitTicketResponse, UUID> {
    List<ExitTicketResponse> findBySessionIdOrderBySubmittedAtAsc(UUID sessionId);
    Optional<ExitTicketResponse> findBySessionIdAndStudentId(UUID sessionId, UUID studentId);
}

