package com.schoolerp.modules.staff.repository;

import com.schoolerp.modules.staff.entity.TeacherCpdRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TeacherCpdRecordRepository extends JpaRepository<TeacherCpdRecord, UUID> {
    List<TeacherCpdRecord> findAllByTenantIdAndTrainingYear(String tenantId, String trainingYear);
    List<TeacherCpdRecord> findAllByTenantIdAndStaffIdAndTrainingYear(String tenantId, UUID staffId, String trainingYear);
    List<TeacherCpdRecord> findAllByTenantIdAndStaffId(String tenantId, UUID staffId);
}

