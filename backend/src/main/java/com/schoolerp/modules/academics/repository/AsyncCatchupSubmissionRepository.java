package com.schoolerp.modules.academics.repository;

import com.schoolerp.modules.academics.entity.AsyncCatchupSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AsyncCatchupSubmissionRepository extends JpaRepository<AsyncCatchupSubmission, UUID> {
    List<AsyncCatchupSubmission> findBySessionIdOrderBySubmittedAtAsc(UUID sessionId);
    List<AsyncCatchupSubmission> findByStudentIdOrderBySubmittedAtDesc(UUID studentId);
    Optional<AsyncCatchupSubmission> findBySessionIdAndStudentId(UUID sessionId, UUID studentId);
}

