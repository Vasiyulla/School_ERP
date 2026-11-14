package com.schoolerp.modules.academics.repository;

import com.schoolerp.modules.academics.entity.PblMilestoneSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PblMilestoneSubmissionRepository extends JpaRepository<PblMilestoneSubmission, UUID> {
    List<PblMilestoneSubmission> findByProjectIdOrderByMilestoneNumberAsc(UUID projectId);
    List<PblMilestoneSubmission> findByProjectIdAndGroupNameOrderByMilestoneNumberAsc(UUID projectId, String groupName);
}

