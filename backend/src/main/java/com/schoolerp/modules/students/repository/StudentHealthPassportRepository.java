package com.schoolerp.modules.students.repository;

import com.schoolerp.modules.students.entity.StudentHealthPassport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudentHealthPassportRepository extends JpaRepository<StudentHealthPassport, UUID> {
    Optional<StudentHealthPassport> findByStudentId(UUID studentId);
}
