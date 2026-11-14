package com.schoolerp.modules.staff.service;

import com.schoolerp.common.context.TenantContext;
import com.schoolerp.modules.staff.dto.*;
import com.schoolerp.modules.staff.entity.DesignationMaster;
import com.schoolerp.modules.staff.entity.Employee;
import com.schoolerp.modules.staff.repository.DesignationMasterRepository;
import com.schoolerp.modules.staff.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DesignationHierarchyService {

    private final DesignationMasterRepository designationRepository;
    private final EmployeeRepository employeeRepository;

    private String resolveTenant() {
        String tenantId = TenantContext.getTenantId();
        return (tenantId != null && !tenantId.isBlank()) ? tenantId : "tenant-default";
    }

    @Transactional
    public void seedStandardDesignationsIfEmpty(String tenantId) {
        if (designationRepository.findAllByTenantId(tenantId).isEmpty()) {
            List<DesignationMaster> presets = List.of(
                    DesignationMaster.builder()
                            .tenantId(tenantId)
                            .designationCode("PRINCIPAL")
                            .title("Principal & Head of School")
                            .department("Executive")
                            .hierarchyTier(1)
                            .reportsToDesignationCode(null)
                            .baseRole("ROLE_PRINCIPAL")
                            .financialApprovalLimit(new BigDecimal("1000000.00"))
                            .defaultCpcLevel("LEVEL_12")
                            .isActive(true)
                            .isSystemStandard(true)
                            .build(),
                    DesignationMaster.builder()
                            .tenantId(tenantId)
                            .designationCode("HEAD_OF_FINANCE")
                            .title("Head of Finance & Treasury")
                            .department("Finance")
                            .hierarchyTier(2)
                            .reportsToDesignationCode("PRINCIPAL")
                            .baseRole("ROLE_ACCOUNTANT")
                            .financialApprovalLimit(new BigDecimal("500000.00"))
                            .defaultCpcLevel("LEVEL_10")
                            .isActive(true)
                            .isSystemStandard(true)
                            .build(),
                    DesignationMaster.builder()
                            .tenantId(tenantId)
                            .designationCode("SR_ACCOUNTANT")
                            .title("Senior Bursar & Accountant")
                            .department("Finance")
                            .hierarchyTier(3)
                            .reportsToDesignationCode("HEAD_OF_FINANCE")
                            .baseRole("ROLE_ACCOUNTANT")
                            .financialApprovalLimit(new BigDecimal("50000.00"))
                            .defaultCpcLevel("LEVEL_7")
                            .isActive(true)
                            .isSystemStandard(true)
                            .build(),
                    DesignationMaster.builder()
                            .tenantId(tenantId)
                            .designationCode("CASHIER")
                            .title("Fee Counter Cashier")
                            .department("Finance")
                            .hierarchyTier(4)
                            .reportsToDesignationCode("SR_ACCOUNTANT")
                            .baseRole("ROLE_ACCOUNTANT")
                            .financialApprovalLimit(new BigDecimal("5000.00"))
                            .defaultCpcLevel("LEVEL_4")
                            .isActive(true)
                            .isSystemStandard(true)
                            .build(),
                    DesignationMaster.builder()
                            .tenantId(tenantId)
                            .designationCode("HOD_SCIENCE")
                            .title("Head of Department (Science)")
                            .department("Academics")
                            .hierarchyTier(2)
                            .reportsToDesignationCode("PRINCIPAL")
                            .baseRole("ROLE_TEACHER")
                            .financialApprovalLimit(new BigDecimal("50000.00"))
                            .defaultCpcLevel("LEVEL_9")
                            .isActive(true)
                            .isSystemStandard(true)
                            .build(),
                    DesignationMaster.builder()
                            .tenantId(tenantId)
                            .designationCode("PGT_PHYSICS")
                            .title("Post Graduate Teacher (PGT Physics)")
                            .department("Academics")
                            .hierarchyTier(3)
                            .reportsToDesignationCode("HOD_SCIENCE")
                            .baseRole("ROLE_TEACHER")
                            .financialApprovalLimit(new BigDecimal("10000.00"))
                            .defaultCpcLevel("LEVEL_8")
                            .isActive(true)
                            .isSystemStandard(true)
                            .build(),
                    DesignationMaster.builder()
                            .tenantId(tenantId)
                            .designationCode("TGT_MATHEMATICS")
                            .title("Trained Graduate Teacher (TGT Maths)")
                            .department("Academics")
                            .hierarchyTier(4)
                            .reportsToDesignationCode("HOD_SCIENCE")
                            .baseRole("ROLE_TEACHER")
                            .financialApprovalLimit(BigDecimal.ZERO)
                            .defaultCpcLevel("LEVEL_7")
                            .isActive(true)
                            .isSystemStandard(true)
                            .build()
            );
            designationRepository.saveAll(presets);
            log.info("Seeded {} standard designations for tenant {}", presets.size(), tenantId);
        }
    }

    @Transactional
    public DesignationMaster createCustomDesignation(CreateDesignationRequest request) {
        String tenantId = resolveTenant();
        seedStandardDesignationsIfEmpty(tenantId);

        String code = request.getDesignationCode().trim().toUpperCase().replaceAll("\\s+", "_");

        Optional<DesignationMaster> existing = designationRepository.findByTenantIdAndDesignationCode(tenantId, code);
        if (existing.isPresent()) {
            throw new IllegalArgumentException("Designation code '" + code + "' already exists for this institution");
        }

        DesignationMaster designation = DesignationMaster.builder()
                .tenantId(tenantId)
                .designationCode(code)
                .title(request.getTitle())
                .department(request.getDepartment() != null ? request.getDepartment() : "General")
                .hierarchyTier(request.getHierarchyTier() != null ? request.getHierarchyTier() : 4)
                .reportsToDesignationCode(request.getReportsToDesignationCode() != null ? request.getReportsToDesignationCode().toUpperCase() : "PRINCIPAL")
                .baseRole(request.getBaseRole() != null ? request.getBaseRole().toUpperCase() : "ROLE_STAFF")
                .financialApprovalLimit(request.getFinancialApprovalLimit() != null ? request.getFinancialApprovalLimit() : BigDecimal.ZERO)
                .defaultCpcLevel(request.getDefaultCpcLevel() != null ? request.getDefaultCpcLevel().toUpperCase() : "LEVEL_6")
                .isActive(true)
                .isSystemStandard(false)
                .build();

        return designationRepository.save(designation);
    }

    @Transactional
    public DesignationMaster updateDesignation(UUID id, UpdateDesignationRequest request) {
        String tenantId = resolveTenant();
        DesignationMaster designation = designationRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Designation not found: " + id));

        if (request.getTitle() != null) designation.setTitle(request.getTitle());
        if (request.getDepartment() != null) designation.setDepartment(request.getDepartment());
        if (request.getHierarchyTier() != null) designation.setHierarchyTier(request.getHierarchyTier());
        if (request.getReportsToDesignationCode() != null) designation.setReportsToDesignationCode(request.getReportsToDesignationCode());
        if (request.getBaseRole() != null) designation.setBaseRole(request.getBaseRole());
        if (request.getFinancialApprovalLimit() != null) designation.setFinancialApprovalLimit(request.getFinancialApprovalLimit());
        if (request.getDefaultCpcLevel() != null) designation.setDefaultCpcLevel(request.getDefaultCpcLevel());
        if (request.getIsActive() != null) designation.setActive(request.getIsActive());

        return designationRepository.save(designation);
    }

    @Transactional
    public DesignationMaster toggleDesignationStatus(UUID id, boolean isActive) {
        String tenantId = resolveTenant();
        DesignationMaster designation = designationRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Designation not found: " + id));

        designation.setActive(isActive);
        return designationRepository.save(designation);
    }

    @Transactional
    public List<DesignationMaster> getDesignations(String department, Boolean activeOnly) {
        String tenantId = resolveTenant();
        seedStandardDesignationsIfEmpty(tenantId);

        List<DesignationMaster> all = designationRepository.findAllByTenantId(tenantId);
        return all.stream()
                .filter(d -> department == null || d.getDepartment().equalsIgnoreCase(department))
                .filter(d -> activeOnly == null || !activeOnly || d.isActive())
                .sorted(Comparator.comparingInt(DesignationMaster::getHierarchyTier))
                .collect(Collectors.toList());
    }

    @Transactional
    public List<DesignationHierarchyNodeDTO> getOrganizationalHierarchyTree() {
        String tenantId = resolveTenant();
        seedStandardDesignationsIfEmpty(tenantId);

        List<DesignationMaster> allActive = designationRepository.findAllByTenantIdAndIsActiveTrue(tenantId);
        List<Employee> allEmployees = employeeRepository.findAllByTenantId(tenantId);

        // Count employees per designation
        Map<String, Long> countMap = allEmployees.stream()
                .collect(Collectors.groupingBy(e -> e.getDesignation().toUpperCase(), Collectors.counting()));

        // Map DTOs
        Map<String, DesignationHierarchyNodeDTO> nodeMap = new HashMap<>();
        for (DesignationMaster dm : allActive) {
            int empCount = countMap.getOrDefault(dm.getDesignationCode().toUpperCase(),
                    countMap.getOrDefault(dm.getTitle().toUpperCase(), 0L)).intValue();

            nodeMap.put(dm.getDesignationCode().toUpperCase(), DesignationHierarchyNodeDTO.builder()
                    .designationCode(dm.getDesignationCode())
                    .title(dm.getTitle())
                    .department(dm.getDepartment())
                    .hierarchyTier(dm.getHierarchyTier())
                    .reportsTo(dm.getReportsToDesignationCode())
                    .baseRole(dm.getBaseRole())
                    .approvalLimitInr(dm.getFinancialApprovalLimit())
                    .activeEmployeeCount(empCount)
                    .subordinates(new ArrayList<>())
                    .build());
        }

        // Build tree
        List<DesignationHierarchyNodeDTO> roots = new ArrayList<>();
        for (DesignationHierarchyNodeDTO node : nodeMap.values()) {
            if (node.getReportsTo() == null || !nodeMap.containsKey(node.getReportsTo().toUpperCase())
                    || node.getReportsTo().equalsIgnoreCase(node.getDesignationCode())) {
                roots.add(node);
            } else {
                DesignationHierarchyNodeDTO parent = nodeMap.get(node.getReportsTo().toUpperCase());
                if (parent != null) {
                    parent.getSubordinates().add(node);
                } else {
                    roots.add(node);
                }
            }
        }

        roots.sort(Comparator.comparingInt(DesignationHierarchyNodeDTO::getHierarchyTier));
        return roots;
    }

    @Transactional
    public ApprovalAuthorityResultDTO validateApprovalAuthority(CheckApprovalAuthorityRequest request) {
        String tenantId = resolveTenant();
        seedStandardDesignationsIfEmpty(tenantId);

        String empCode = request.getEmployeeCode() != null ? request.getEmployeeCode() : "EMP-001";
        BigDecimal amount = request.getAmount() != null ? request.getAmount() : BigDecimal.ZERO;

        Employee employee = employeeRepository.findByTenantIdAndEmployeeCode(tenantId, empCode)
                .orElse(null);

        String employeeName = (employee != null) ? employee.getFullName() : "Staff Member";
        String desigName = (employee != null) ? employee.getDesignation() : "ACCOUNTANT";

        // Find designation matching title or code
        List<DesignationMaster> all = designationRepository.findAllByTenantId(tenantId);
        DesignationMaster desig = all.stream()
                .filter(d -> d.getDesignationCode().equalsIgnoreCase(desigName) || d.getTitle().equalsIgnoreCase(desigName))
                .findFirst()
                .orElse(null);

        BigDecimal limit = (desig != null) ? desig.getFinancialApprovalLimit() : new BigDecimal("25000.00");
        String desigCode = (desig != null) ? desig.getDesignationCode() : desigName;
        String title = (desig != null) ? desig.getTitle() : desigName;
        String superior = (desig != null && desig.getReportsToDesignationCode() != null)
                ? desig.getReportsToDesignationCode() : "PRINCIPAL";

        boolean authorized = amount.compareTo(limit) <= 0;
        String status = authorized ? "AUTHORIZED" : "ESCALATION_REQUIRED";
        String message = authorized
                ? "Expenditure of INR " + amount + " is within authorized approval threshold of INR " + limit + " for " + title
                : "Expenditure of INR " + amount + " exceeds approval limit of INR " + limit + ". Must escalate to superior: " + superior;

        return ApprovalAuthorityResultDTO.builder()
                .employeeCode(empCode)
                .employeeName(employeeName)
                .designationCode(desigCode)
                .designationTitle(title)
                .requestedAmount(amount)
                .approvalLimitInr(limit)
                .status(status)
                .escalateToDesignation(authorized ? null : superior)
                .message(message)
                .build();
    }
}

