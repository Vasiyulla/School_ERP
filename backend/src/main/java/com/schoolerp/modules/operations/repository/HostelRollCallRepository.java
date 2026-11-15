package com.schoolerp.modules.operations.repository;

import com.schoolerp.modules.operations.entity.HostelRollCall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface HostelRollCallRepository extends JpaRepository<HostelRollCall, UUID> {
    List<HostelRollCall> findByTenantIdAndRollCallDateAndSessionType(String tenantId, LocalDate rollCallDate, String sessionType);
    List<HostelRollCall> findByTenantIdAndStudentId(String tenantId, UUID studentId);
}

