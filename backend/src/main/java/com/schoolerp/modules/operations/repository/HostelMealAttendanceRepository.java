package com.schoolerp.modules.operations.repository;

import com.schoolerp.modules.operations.entity.HostelMealAttendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface HostelMealAttendanceRepository extends JpaRepository<HostelMealAttendance, UUID> {
    Optional<HostelMealAttendance> findByTenantIdAndStudentIdAndMealDateAndMealType(String tenantId, UUID studentId, LocalDate mealDate, String mealType);
    List<HostelMealAttendance> findAllByTenantIdAndMealDate(String tenantId, LocalDate mealDate);
}
