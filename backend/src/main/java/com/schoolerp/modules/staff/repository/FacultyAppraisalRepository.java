package com.schoolerp.modules.staff.repository;

import com.schoolerp.modules.staff.entity.FacultyAppraisal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FacultyAppraisalRepository extends JpaRepository<FacultyAppraisal, UUID> {

    List<FacultyAppraisal> findAllByTenantIdOrderByCompositeScoreDesc(String tenantId);

    List<FacultyAppraisal> findAllByTenantIdAndAcademicYearOrderByCompositeScoreDesc(String tenantId, String academicYear);

    Optional<FacultyAppraisal> findByTenantIdAndTeacherIdAndAcademicYear(String tenantId, UUID teacherId, String academicYear);

    Optional<FacultyAppraisal> findByIdAndTenantId(UUID id, String tenantId);
}

