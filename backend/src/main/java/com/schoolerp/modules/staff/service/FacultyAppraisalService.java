package com.schoolerp.modules.staff.service;

import com.schoolerp.common.context.TenantContext;
import com.schoolerp.modules.staff.dto.EvaluateFacultyAppraisalRequest;
import com.schoolerp.modules.staff.entity.Employee;
import com.schoolerp.modules.staff.entity.FacultyAppraisal;
import com.schoolerp.modules.staff.repository.EmployeeRepository;
import com.schoolerp.modules.staff.repository.FacultyAppraisalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FacultyAppraisalService {

    private final FacultyAppraisalRepository appraisalRepository;
    private final EmployeeRepository employeeRepository;

    private String resolveTenant() {
        String tenantId = TenantContext.getTenantId();
        return (tenantId != null && !tenantId.isBlank()) ? tenantId : "tenant-default";
    }

    @Transactional
    public FacultyAppraisal evaluateTeacher(EvaluateFacultyAppraisalRequest request, String principalStaffName) {
        String tenantId = resolveTenant();

        Employee employee = employeeRepository.findByIdAndTenantId(request.getTeacherId(), tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Faculty member not found with ID: " + request.getTeacherId()));

        double composite = request.getSelfAppraisalScore()
                + request.getAcademicResultsScore()
                + request.getInstitutionalDutyScore()
                + request.getPrincipalObservationScore();

        String band;
        double increment;

        if (composite >= 90.0) {
            band = "EXEMPLARY_A_PLUS";
            increment = 10.0;
        } else if (composite >= 80.0) {
            band = "COMMENDABLE_A";
            increment = 7.5;
        } else if (composite >= 70.0) {
            band = "SATISFACTORY_B";
            increment = 5.0;
        } else {
            band = "NEEDS_IMPROVEMENT_C";
            increment = 0.0;
        }

        Optional<FacultyAppraisal> optExisting = appraisalRepository.findByTenantIdAndTeacherIdAndAcademicYear(
                tenantId, employee.getId(), request.getAcademicYear());

        FacultyAppraisal appraisal;
        if (optExisting.isPresent()) {
            appraisal = optExisting.get();
            appraisal.setSelfAppraisalScore(request.getSelfAppraisalScore());
            appraisal.setAcademicResultsScore(request.getAcademicResultsScore());
            appraisal.setInstitutionalDutyScore(request.getInstitutionalDutyScore());
            appraisal.setPrincipalObservationScore(request.getPrincipalObservationScore());
            appraisal.setCompositeScore(composite);
            appraisal.setAppraisalBand(band);
            appraisal.setRecommendedIncrementPercentage(increment);
            appraisal.setPrincipalEvaluationNotes(request.getPrincipalEvaluationNotes());
            appraisal.setEvaluatedByStaffName(principalStaffName);
            appraisal.setEvaluatedAt(LocalDateTime.now());
        } else {
            appraisal = FacultyAppraisal.builder()
                    .tenantId(tenantId)
                    .academicYear(request.getAcademicYear())
                    .teacherId(employee.getId())
                    .teacherName(employee.getFullName())
                    .department(employee.getDepartment())
                    .designation(employee.getDesignation())
                    .selfAppraisalScore(request.getSelfAppraisalScore())
                    .academicResultsScore(request.getAcademicResultsScore())
                    .institutionalDutyScore(request.getInstitutionalDutyScore())
                    .principalObservationScore(request.getPrincipalObservationScore())
                    .compositeScore(composite)
                    .appraisalBand(band)
                    .recommendedIncrementPercentage(increment)
                    .principalEvaluationNotes(request.getPrincipalEvaluationNotes())
                    .evaluatedByStaffName(principalStaffName)
                    .build();
        }

        log.info("Faculty appraisal completed for {}: Score={}/100, Band={}, Recommended Increment={}% by Principal {}",
                employee.getFullName(), composite, band, increment, principalStaffName);

        return appraisalRepository.save(appraisal);
    }

    @Transactional(readOnly = true)
    public List<FacultyAppraisal> getAppraisals(String academicYear) {
        String tenantId = resolveTenant();
        if (academicYear != null && !academicYear.isBlank() && !"ALL".equalsIgnoreCase(academicYear)) {
            return appraisalRepository.findAllByTenantIdAndAcademicYearOrderByCompositeScoreDesc(tenantId, academicYear);
        }
        return appraisalRepository.findAllByTenantIdOrderByCompositeScoreDesc(tenantId);
    }
}

