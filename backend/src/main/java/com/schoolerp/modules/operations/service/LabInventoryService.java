package com.schoolerp.modules.operations.service;

import com.schoolerp.common.context.TenantContext;
import com.schoolerp.modules.finance.entity.FeeInvoice;
import com.schoolerp.modules.finance.repository.FeeInvoiceRepository;
import com.schoolerp.modules.operations.dto.RecordLabBreakageRequest;
import com.schoolerp.modules.operations.entity.ApparatusBreakage;
import com.schoolerp.modules.operations.entity.LabItem;
import com.schoolerp.modules.operations.repository.ApparatusBreakageRepository;
import com.schoolerp.modules.operations.repository.LabItemRepository;
import com.schoolerp.modules.students.entity.Student;
import com.schoolerp.modules.students.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class LabInventoryService {

    private final LabItemRepository labItemRepository;
    private final ApparatusBreakageRepository breakageRepository;
    private final StudentRepository studentRepository;
    private final FeeInvoiceRepository feeInvoiceRepository;

    private String resolveTenant() {
        String tenantId = TenantContext.getTenantId();
        return (tenantId != null && !tenantId.isBlank()) ? tenantId : "tenant-default";
    }

    @Transactional
    public List<LabItem> getInventory(String labName) {
        String tenantId = resolveTenant();

        List<LabItem> items;
        if (labName != null && !labName.isBlank() && !"ALL".equalsIgnoreCase(labName)) {
            items = labItemRepository.findAllByTenantIdAndLabName(tenantId, labName);
        } else {
            items = labItemRepository.findAllByTenantIdOrderByLabNameAsc(tenantId);
        }

        if (items.isEmpty()) {
            items = seedDefaultLabInventory(tenantId);
        }

        return items;
    }

    @Transactional
    public ApparatusBreakage recordBreakage(RecordLabBreakageRequest request, String technicianStaffName) {
        String tenantId = resolveTenant();

        LabItem labItem = labItemRepository.findByIdAndTenantId(request.getLabItemId(), tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Lab apparatus not found with ID: " + request.getLabItemId()));

        Student student = studentRepository.findByIdAndTenantId(request.getStudentId(), tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + request.getStudentId()));

        // 1. Decrement physical inventory count
        if (labItem.getQuantityOnHand() > 0) {
            labItem.setQuantityOnHand(labItem.getQuantityOnHand() - 1);
            labItemRepository.save(labItem);
        }

        // 2. Generate Auxiliary Breakage Fee Invoice in Finance ledger
        String invoiceNum = String.format("INV-BRK-%d-%s",
                LocalDate.now().getYear(),
                UUID.randomUUID().toString().substring(0, 6).toUpperCase());

        FeeInvoice breakageInvoice = FeeInvoice.builder()
                .tenantId(tenantId)
                .invoiceNumber(invoiceNum)
                .studentId(student.getId())
                .studentName(student.getFullName())
                .totalAmount(request.getAssessedDamageCost())
                .paidAmount(BigDecimal.ZERO)
                .balanceAmount(request.getAssessedDamageCost())
                .dueDate(LocalDate.now().plusDays(14))
                .status("OVERDUE")
                .build();
        feeInvoiceRepository.save(breakageInvoice);

        // 3. Update student outstanding balance
        student.setBalanceAmount(student.getBalanceAmount() + request.getAssessedDamageCost().doubleValue());
        student.setFeeStatus("Partial");
        studentRepository.save(student);

        // 4. Save Breakage Audit Record
        ApparatusBreakage breakage = ApparatusBreakage.builder()
                .tenantId(tenantId)
                .labItemId(labItem.getId())
                .itemName(labItem.getItemName())
                .labName(labItem.getLabName())
                .studentId(student.getId())
                .studentAdmissionNo(student.getAdmissionNumber())
                .studentName(student.getFullName())
                .className(student.getClassName() + " - " + student.getSectionName())
                .incidentDate(request.getIncidentDate())
                .faultType(request.getFaultType() != null ? request.getFaultType().toUpperCase() : "ACCIDENTAL")
                .assessedDamageCost(request.getAssessedDamageCost())
                .fineStatus("INVOICED_TO_STUDENT")
                .invoiceNumberGenerated(invoiceNum)
                .labTechnicianStaffName(technicianStaffName != null ? technicianStaffName : "Lab Assistant")
                .remarks(request.getRemarks())
                .build();

        log.info("Lab breakage recorded for student {}: Item '{}' in {}. Invoice generated: {} for INR {}",
                student.getFullName(), labItem.getItemName(), labItem.getLabName(), invoiceNum, request.getAssessedDamageCost());

        return breakageRepository.save(breakage);
    }

    @Transactional(readOnly = true)
    public List<ApparatusBreakage> getBreakages(String labName) {
        String tenantId = resolveTenant();
        if (labName != null && !labName.isBlank() && !"ALL".equalsIgnoreCase(labName)) {
            return breakageRepository.findAllByTenantIdAndLabName(tenantId, labName);
        }
        return breakageRepository.findAllByTenantIdOrderByCreatedAtDesc(tenantId);
    }

    private List<LabItem> seedDefaultLabInventory(String tenantId) {
        List<LabItem> defaults = List.of(
                LabItem.builder()
                        .tenantId(tenantId)
                        .labName("Chemistry Lab")
                        .itemName("Borosilicate Class-A Burette 50ml")
                        .specification("PTFE Stopcock, DIN EN ISO 385")
                        .quantityOnHand(30)
                        .unitCost(new BigDecimal("850.00"))
                        .reorderThreshold(5)
                        .isHazardous(false)
                        .build(),
                LabItem.builder()
                        .tenantId(tenantId)
                        .labName("Biology Lab")
                        .itemName("Binocular Compound Microscope 1000x")
                        .specification("Achromatic DIN Optics, LED Illuminator")
                        .quantityOnHand(15)
                        .unitCost(new BigDecimal("12500.00"))
                        .reorderThreshold(2)
                        .isHazardous(false)
                        .build(),
                LabItem.builder()
                        .tenantId(tenantId)
                        .labName("Physics Lab")
                        .itemName("Precision Optical Bench with Crown Glass Prisms")
                        .specification("1.5 Meter Stainless Steel Rail with Bi-convex Lens set")
                        .quantityOnHand(12)
                        .unitCost(new BigDecimal("4200.00"))
                        .reorderThreshold(2)
                        .isHazardous(false)
                        .build(),
                LabItem.builder()
                        .tenantId(tenantId)
                        .labName("Robotics STEM Lab")
                        .itemName("Arduino Mega 2560 Sensor & IoT Kit")
                        .specification("Includes ultrasonic, servo, accelerometer modules")
                        .quantityOnHand(25)
                        .unitCost(new BigDecimal("3500.00"))
                        .reorderThreshold(4)
                        .isHazardous(false)
                        .build()
        );
        return labItemRepository.saveAll(defaults);
    }
}

