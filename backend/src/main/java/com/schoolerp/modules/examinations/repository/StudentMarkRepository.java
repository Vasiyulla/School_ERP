package com.schoolerp.modules.examinations.repository;

import com.schoolerp.modules.examinations.entity.StudentMark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudentMarkRepository extends JpaRepository<StudentMark, UUID> {
    List<StudentMark> findAllByTenantIdAndTermNameAndSubject(String tenantId, String termName, String subject);
    Optional<StudentMark> findByTenantIdAndTermNameAndStudentIdAndSubject(String tenantId, String termName, UUID studentId, String subject);
    List<StudentMark> findAllByTenantIdAndStudentIdAndTermName(String tenantId, UUID studentId, String termName);
    List<StudentMark> findAllByTenantIdAndTermName(String tenantId, String termName);
    List<StudentMark> findAllByTenantIdAndStudentId(String tenantId, UUID studentId);
}

