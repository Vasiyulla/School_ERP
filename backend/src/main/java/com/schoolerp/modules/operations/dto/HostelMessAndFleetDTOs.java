package com.schoolerp.modules.operations.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class HostelMessAndFleetDTOs {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConfigureMessMenuRequest {
        private String dayOfWeek;
        private String mealType;
        private String menuItems;
        private String dietaryType;
        private String allergensContained;
        private String specialDietNote;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MealPunchRequest {
        private UUID studentId;
        private String studentName;
        private String rfidCardUid;
        private String mealType;
        private String diningHallGate;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MealPunchResponseDTO {
        private boolean accepted;
        private String studentName;
        private String mealType;
        private String diningHallGate;
        private String message;
        private String timestamp;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RoomInspectionRequest {
        private UUID studentId;
        private String studentName;
        private String roomNumber;
        private String inspectionType;
        private String bedFrameStatus;
        private String mattressStatus;
        private String studyTableStatus;
        private String wardrobeStatus;
        private String electricalFixturesStatus;
        private BigDecimal assessedDamageFee;
        private String inspectorRemarks;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmergencyBusSwapRequest {
        private String brokenVehicleReg;
        private String replacementVehicleReg;
        private String reliefDriverName;
        private String reliefDriverPhone;
        private String routeCode;
        private String breakdownReason;
        private int affectedStudentsCount;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DriverPreTripCheckRequest {
        private String driverEmployeeId;
        private String driverName;
        private String vehicleRegistration;
        private BigDecimal breathalyzerReading;
        private boolean licenseValid;
        private boolean tirePressureOk;
        private boolean firstAidKitPresent;
        private boolean fireExtinguisherPresent;
        private boolean emergencyDoorOperational;
        private String supervisorSignature;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DailyMessMenuOverviewDTO {
        private String dayOfWeek;
        private String date;
        private List<HostelMessMenuDTO> meals;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HostelMessMenuDTO {
        private UUID id;
        private String mealType;
        private String menuItems;
        private String dietaryType;
        private String allergensContained;
    }
}
