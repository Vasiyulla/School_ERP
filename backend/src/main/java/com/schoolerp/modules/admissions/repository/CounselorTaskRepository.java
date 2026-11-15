package com.schoolerp.modules.admissions.repository;

import com.schoolerp.modules.admissions.entity.CounselorTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CounselorTaskRepository extends JpaRepository<CounselorTask, UUID> {
    List<CounselorTask> findByTenantIdAndCounselorNameOrderByDueDateAsc(String tenantId, String counselorName);
    List<CounselorTask> findByTenantIdAndStatusOrderByDueDateAsc(String tenantId, String status);
    List<CounselorTask> findByTenantIdAndInquiryIdOrderByDueDateAsc(String tenantId, UUID inquiryId);
    List<CounselorTask> findByTenantIdOrderByDueDateAsc(String tenantId);
    Optional<CounselorTask> findByIdAndTenantId(UUID id, String tenantId);
}

