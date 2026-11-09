package com.schoolerp.modules.examinations.repository;

import com.schoolerp.modules.examinations.entity.CbseCandidateLoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CbseCandidateLocRepository extends JpaRepository<CbseCandidateLoc, UUID> {
    List<CbseCandidateLoc> findByTenantId(String tenantId);
    List<CbseCandidateLoc> findByTenantIdAndClassStandard(String tenantId, String classStandard);
}

