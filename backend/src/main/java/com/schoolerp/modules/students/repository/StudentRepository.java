package com.schoolerp.modules.students.repository;

import com.schoolerp.modules.students.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudentRepository extends JpaRepository<Student, UUID> {

    List<Student> findAllByTenantId(String tenantId);

    Page<Student> findAllByTenantId(String tenantId, Pageable pageable);

    Optional<Student> findByIdAndTenantId(UUID id, String tenantId);

    Optional<Student> findByAdmissionNumberAndTenantId(String admissionNumber, String tenantId);

    List<Student> findAllByTenantIdAndClassNameAndSectionName(String tenantId, String className, String sectionName);

    List<Student> findAllByTenantIdAndGuardianEmail(String tenantId, String guardianEmail);

    long countByTenantId(String tenantId);
}

