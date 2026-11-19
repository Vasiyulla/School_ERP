package com.schoolerp.modules.operations.service;

import com.schoolerp.common.context.TenantContext;
import com.schoolerp.modules.operations.dto.HostelMessAndFleetDTOs;
import com.schoolerp.modules.operations.entity.HostelMealAttendance;
import com.schoolerp.modules.operations.entity.HostelMessMenu;
import com.schoolerp.modules.operations.entity.HostelRoomInspection;
import com.schoolerp.modules.operations.repository.HostelMealAttendanceRepository;
import com.schoolerp.modules.operations.repository.HostelMessMenuRepository;
import com.schoolerp.modules.operations.repository.HostelRoomInspectionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class HostelMessService {

    private final HostelMessMenuRepository menuRepository;
    private final HostelMealAttendanceRepository mealAttendanceRepository;
    private final HostelRoomInspectionRepository roomInspectionRepository;

    @Transactional
    public HostelMessMenu configureMenu(HostelMessAndFleetDTOs.ConfigureMessMenuRequest req) {
        String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "apex";
        String day = req.getDayOfWeek() != null ? req.getDayOfWeek().toUpperCase() : "MONDAY";
        String meal = req.getMealType() != null ? req.getMealType().toUpperCase() : "LUNCH";

        HostelMessMenu menu = menuRepository.findByTenantIdAndDayOfWeekAndMealType(tenantId, day, meal)
                .orElse(HostelMessMenu.builder()
                        .tenantId(tenantId)
                        .dayOfWeek(day)
                        .mealType(meal)
                        .build());

        menu.setMenuItems(req.getMenuItems() != null ? req.getMenuItems() : "Standard Balanced Nutritional Meal");
        menu.setDietaryType(req.getDietaryType() != null ? req.getDietaryType() : "VEGETARIAN");
        menu.setAllergensContained(req.getAllergensContained());
        menu.setSpecialDietNote(req.getSpecialDietNote());

        return menuRepository.save(menu);
    }

    @Transactional
    public HostelMessAndFleetDTOs.DailyMessMenuOverviewDTO getTodayMenu() {
        String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "apex";
        DayOfWeek dow = LocalDate.now().getDayOfWeek();
        String todayDay = dow.name();

        List<HostelMessMenu> meals = menuRepository.findAllByTenantIdAndDayOfWeek(tenantId, todayDay);
        if (meals.isEmpty()) {
            // Seed standard daily 4-meal cycle for today
            HostelMessMenu breakfast = HostelMessMenu.builder()
                    .tenantId(tenantId)
                    .dayOfWeek(todayDay)
                    .mealType("BREAKFAST")
                    .menuItems("Steamed Idli, Medu Vada, Coconut Chutney, Sambhar, Hard Boiled Eggs, Papaya Slices, Milk & Tea")
                    .dietaryType("VEGETARIAN")
                    .allergensContained("Dairy")
                    .build();
            HostelMessMenu lunch = HostelMessMenu.builder()
                    .tenantId(tenantId)
                    .dayOfWeek(todayDay)
                    .mealType("LUNCH")
                    .menuItems("Paneer Butter Masala / Chicken Curry, Dal Tadka, Jeera Rice, Tandoori Roti, Cucumber Salad, Curd")
                    .dietaryType("VEGETARIAN")
                    .allergensContained("Dairy, Gluten")
                    .build();
            HostelMessMenu dinner = HostelMessMenu.builder()
                    .tenantId(tenantId)
                    .dayOfWeek(todayDay)
                    .mealType("DINNER")
                    .menuItems("Mixed Vegetable Korma, Steamed Basmati Rice, Chapati, Dal Makhani, Gulab Jamun")
                    .dietaryType("VEGETARIAN")
                    .allergensContained("Dairy, Gluten")
                    .build();

            menuRepository.saveAll(List.of(breakfast, lunch, dinner));
            meals = List.of(breakfast, lunch, dinner);
        }

        List<HostelMessAndFleetDTOs.HostelMessMenuDTO> mealDTOs = meals.stream()
                .map(m -> HostelMessAndFleetDTOs.HostelMessMenuDTO.builder()
                        .id(m.getId())
                        .mealType(m.getMealType())
                        .menuItems(m.getMenuItems())
                        .dietaryType(m.getDietaryType())
                        .allergensContained(m.getAllergensContained())
                        .build())
                .collect(Collectors.toList());

        return HostelMessAndFleetDTOs.DailyMessMenuOverviewDTO.builder()
                .dayOfWeek(todayDay)
                .date(LocalDate.now().toString())
                .meals(mealDTOs)
                .build();
    }

    @Transactional
    public HostelMessAndFleetDTOs.MealPunchResponseDTO recordMealPunch(HostelMessAndFleetDTOs.MealPunchRequest req) {
        String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "apex";
        UUID studentId = req.getStudentId() != null ? req.getStudentId() : UUID.randomUUID();
        LocalDate today = LocalDate.now();
        String mealType = req.getMealType() != null ? req.getMealType().toUpperCase() : "LUNCH";

        // Double-dipping defense: Check if student already swiped for this meal window today
        var existingOpt = mealAttendanceRepository.findByTenantIdAndStudentIdAndMealDateAndMealType(tenantId, studentId, today, mealType);
        if (existingOpt.isPresent()) {
            return HostelMessAndFleetDTOs.MealPunchResponseDTO.builder()
                    .accepted(false)
                    .studentName(req.getStudentName() != null ? req.getStudentName() : "Student")
                    .mealType(mealType)
                    .diningHallGate(req.getDiningHallGate() != null ? req.getDiningHallGate() : "MAIN_GATE")
                    .message("REJECTED: Duplicate swipe detected! Student has already consumed " + mealType + " today at " + existingOpt.get().getPunchedAt())
                    .timestamp(Instant.now().toString())
                    .build();
        }

        HostelMealAttendance punch = HostelMealAttendance.builder()
                .tenantId(tenantId)
                .studentId(studentId)
                .studentName(req.getStudentName() != null ? req.getStudentName() : "Student")
                .rfidCardUid(req.getRfidCardUid() != null ? req.getRfidCardUid() : "RFID-MIFARE-DORM-01")
                .mealDate(today)
                .mealType(mealType)
                .diningHallGate(req.getDiningHallGate() != null ? req.getDiningHallGate() : "TURNSTILE-GATE-01")
                .build();

        mealAttendanceRepository.save(punch);

        return HostelMessAndFleetDTOs.MealPunchResponseDTO.builder()
                .accepted(true)
                .studentName(punch.getStudentName())
                .mealType(mealType)
                .diningHallGate(punch.getDiningHallGate())
                .message("ACCEPTED: Meal punch verified. Enjoy your meal!")
                .timestamp(Instant.now().toString())
                .build();
    }

    @Transactional
    public HostelRoomInspection recordRoomInspection(HostelMessAndFleetDTOs.RoomInspectionRequest req) {
        String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "apex";
        BigDecimal damageFee = req.getAssessedDamageFee() != null ? req.getAssessedDamageFee() : BigDecimal.ZERO;
        boolean deductCaution = damageFee.compareTo(BigDecimal.ZERO) > 0;

        HostelRoomInspection inspection = HostelRoomInspection.builder()
                .tenantId(tenantId)
                .studentId(req.getStudentId() != null ? req.getStudentId() : UUID.randomUUID())
                .studentName(req.getStudentName() != null ? req.getStudentName() : "Residential Student")
                .roomNumber(req.getRoomNumber() != null ? req.getRoomNumber() : "B-204")
                .inspectionType(req.getInspectionType() != null ? req.getInspectionType() : "TERM_END_CHECKOUT")
                .bedFrameStatus(req.getBedFrameStatus() != null ? req.getBedFrameStatus() : "INTACT")
                .mattressStatus(req.getMattressStatus() != null ? req.getMattressStatus() : "INTACT")
                .studyTableStatus(req.getStudyTableStatus() != null ? req.getStudyTableStatus() : "INTACT")
                .wardrobeStatus(req.getWardrobeStatus() != null ? req.getWardrobeStatus() : "INTACT")
                .electricalFixturesStatus(req.getElectricalFixturesStatus() != null ? req.getElectricalFixturesStatus() : "INTACT")
                .assessedDamageFee(damageFee)
                .cautionMoneyDeductionApplied(deductCaution)
                .inspectorRemarks(req.getInspectorRemarks() != null ? req.getInspectorRemarks() : "Checkout fixture audit completed successfully")
                .build();

        return roomInspectionRepository.save(inspection);
    }
}
