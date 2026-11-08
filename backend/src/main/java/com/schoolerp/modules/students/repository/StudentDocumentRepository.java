package com.schoolerp.modules.students.repository;

import com.schoolerp.modules.students.entity.StudentDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudentDocumentRepository extends JpaRepository<StudentDocument, UUID> {
    List<StudentDocument> findAllByTenantIdAndStudentId(String tenantId, UUID studentId);
    Optional<StudentDocument> findByIdAndTenantId(UUID id, String tenantId);
}

