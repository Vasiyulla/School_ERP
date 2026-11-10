package com.schoolerp.modules.academics.repository;

import com.schoolerp.modules.academics.entity.SyllabusTopicProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SyllabusTopicProgressRepository extends JpaRepository<SyllabusTopicProgress, UUID> {
    List<SyllabusTopicProgress> findByTenantIdOrderByUpdatedAtDesc(String tenantId);
    Optional<SyllabusTopicProgress> findTopByTenantIdAndClassNameAndSubjectOrderByUpdatedAtDesc(
            String tenantId, String className, String subject);
}

