package com.schoolerp.modules.operations.service;

import com.schoolerp.modules.operations.dto.EventCostumeRentalDTO;
import com.schoolerp.modules.operations.dto.IssueCostumeRequest;
import com.schoolerp.modules.operations.dto.RegisterCostumeRequest;
import com.schoolerp.modules.operations.entity.EventCostumeRental;
import com.schoolerp.modules.operations.repository.EventCostumeRentalRepository;
import com.schoolerp.modules.students.entity.Student;
import com.schoolerp.modules.students.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CostumeRentalService {

    private final EventCostumeRentalRepository costumeRentalRepository;
    private final StudentRepository studentRepository;

    @Transactional
    public EventCostumeRentalDTO registerCostume(RegisterCostumeRequest req) {
        String code = req.getCostumeCode() != null ? req.getCostumeCode()
                : "CST-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();

        EventCostumeRental costume = EventCostumeRental.builder()
                .tenantId("tenant_apex_mumbai_01")
                .eventId(req.getEventId() != null ? req.getEventId() : UUID.randomUUID())
                .eventName(req.getEventName() != null ? req.getEventName() : "Annual Cultural Day 2026")
                .costumeCode(code)
                .costumeDescription(req.getCostumeDescription() != null ? req.getCostumeDescription() : "Standard Theatrical Outfit")
                .vendorSupplierName(req.getVendorSupplierName() != null ? req.getVendorSupplierName() : "Rangmanch Costumers")
                .rentalRatePerDay(req.getRentalRatePerDay() != null ? req.getRentalRatePerDay() : BigDecimal.ZERO)
                .returnCondition("IN_WARDROBE")
                .damageFineAmount(BigDecimal.ZERO)
                .createdAt(LocalDateTime.now())
                .build();

        EventCostumeRental saved = costumeRentalRepository.save(costume);
        log.info("Event costume registered: {} - {} for event '{}'", saved.getCostumeCode(), saved.getCostumeDescription(), saved.getEventName());
        return mapToDTO(saved);
    }

    @Transactional
    public EventCostumeRentalDTO issueOrReturnCostume(IssueCostumeRequest req) {
        EventCostumeRental costume = costumeRentalRepository.findById(req.getCostumeId())
                .orElseThrow(() -> new IllegalArgumentException("Costume record not found: " + req.getCostumeId()));

        if ("ISSUE".equalsIgnoreCase(req.getAction())) {
            if ("ISSUED_OUT".equalsIgnoreCase(costume.getReturnCondition())) {
                throw new IllegalStateException("Costume " + costume.getCostumeCode() + " is already issued to " + costume.getAssignedStudentName());
            }
            Student student = studentRepository.findById(req.getStudentId())
                    .orElseThrow(() -> new IllegalArgumentException("Student not found: " + req.getStudentId()));

            costume.setAssignedStudentId(student.getId());
            costume.setAssignedStudentName(student.getFullName());
            costume.setReturnCondition("ISSUED_OUT");
            costume.setIssuedDate(LocalDate.now());
            costume.setExpectedReturnDate(req.getExpectedReturnDate() != null ? req.getExpectedReturnDate() : LocalDate.now().plusDays(3));
            costume.setActualReturnDate(null);
            costume.setDamageFineAmount(BigDecimal.ZERO);
            log.info("Costume {} issued to student {}", costume.getCostumeCode(), student.getFullName());
        } else if ("RETURN".equalsIgnoreCase(req.getAction())) {
            costume.setActualReturnDate(LocalDate.now());
            String condition = req.getReturnCondition() != null ? req.getReturnCondition() : "RETURNED_PRISTINE";
            costume.setReturnCondition(condition);

            if ("RETURNED_DAMAGED".equalsIgnoreCase(condition) || "LOST".equalsIgnoreCase(condition)) {
                costume.setDamageFineAmount(req.getDamageFineAmount() != null ? req.getDamageFineAmount() : BigDecimal.valueOf(500));
            } else {
                costume.setDamageFineAmount(BigDecimal.ZERO);
            }
            log.info("Costume {} returned with condition: {}, Damage Fine: {}",
                    costume.getCostumeCode(), costume.getReturnCondition(), costume.getDamageFineAmount());
        } else {
            throw new IllegalArgumentException("Unsupported costume rental action: " + req.getAction());
        }

        EventCostumeRental saved = costumeRentalRepository.save(costume);
        return mapToDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<EventCostumeRentalDTO> getRoster(String eventName, String status) {
        List<EventCostumeRental> list;
        if (eventName != null && !eventName.isBlank()) {
            list = costumeRentalRepository.findByEventNameOrderByCreatedAtDesc(eventName);
        } else if (status != null && !status.isBlank()) {
            list = costumeRentalRepository.findByReturnCondition(status);
        } else {
            list = costumeRentalRepository.findAll();
        }
        return list.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    private EventCostumeRentalDTO mapToDTO(EventCostumeRental c) {
        EventCostumeRentalDTO dto = new EventCostumeRentalDTO();
        dto.setId(c.getId());
        dto.setEventId(c.getEventId());
        dto.setEventName(c.getEventName());
        dto.setCostumeCode(c.getCostumeCode());
        dto.setCostumeDescription(c.getCostumeDescription());
        dto.setVendorSupplierName(c.getVendorSupplierName());
        dto.setAssignedStudentId(c.getAssignedStudentId());
        dto.setAssignedStudentName(c.getAssignedStudentName());
        dto.setRentalRatePerDay(c.getRentalRatePerDay());
        dto.setIssuedDate(c.getIssuedDate());
        dto.setExpectedReturnDate(c.getExpectedReturnDate());
        dto.setActualReturnDate(c.getActualReturnDate());
        dto.setReturnCondition(c.getReturnCondition());
        dto.setDamageFineAmount(c.getDamageFineAmount());
        dto.setCreatedAt(c.getCreatedAt());
        return dto;
    }
}
