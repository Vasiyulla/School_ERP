package com.schoolerp.modules.students.repository;

import com.schoolerp.modules.students.entity.StudentUdiseProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudentUdiseProfileRepository extends JpaRepository<StudentUdiseProfile, UUID> {
    List<StudentUdiseProfile> findByTenantId(String tenantId);
    Optional<StudentUdiseProfile> findByTenantIdAndStudentId(String tenantId, UUID studentId);
}

