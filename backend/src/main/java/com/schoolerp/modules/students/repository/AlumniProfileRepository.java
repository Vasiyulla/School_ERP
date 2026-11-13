package com.schoolerp.modules.students.repository;

import com.schoolerp.modules.students.entity.AlumniProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AlumniProfileRepository extends JpaRepository<AlumniProfile, UUID> {

    List<AlumniProfile> findAllByTenantIdOrderByGraduationYearDesc(String tenantId);

    Optional<AlumniProfile> findByTenantIdAndStudentId(String tenantId, UUID studentId);

    Optional<AlumniProfile> findByIdAndTenantId(UUID id, String tenantId);
}

