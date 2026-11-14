package com.schoolerp.modules.academics.repository;

import com.schoolerp.modules.academics.entity.PblProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PblProjectRepository extends JpaRepository<PblProject, UUID> {
    List<PblProject> findByTenantIdAndGradeLevelOrderByCreatedAtDesc(String tenantId, String gradeLevel);
    List<PblProject> findByTenantIdOrderByCreatedAtDesc(String tenantId);
}

