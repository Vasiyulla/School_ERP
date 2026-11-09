package com.schoolerp.modules.mobile.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParentHomeDashboardDTO {

    private String guardianName;
    private String guardianEmail;
    private String guardianPhone;
    private int totalChildren;
    private BigDecimal totalFamilyPendingFees;
    private int unreadAnnouncementsCount;
    private List<ChildCardDTO> children;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChildCardDTO {
        private UUID studentId;
        private String fullName;
        private String admissionNumber;
        private String className;
        private String sectionName;
        private int rollNumber;
        private double attendancePercentage;
        private BigDecimal pendingFeeBalance;
        private int dueHomeworkCount;
        private BusLiveStatusDTO busStatus;
        private GatePassStatusDTO gatePassStatus;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BusLiveStatusDTO {
        private String vehicleRegistration;
        private String routeName;
        private double currentLatitude;
        private double currentLongitude;
        private double speedKmph;
        private String nearestStop;
        private double distanceMeters;
        private boolean isWithinPickupGeofence;
        private String statusText;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GatePassStatusDTO {
        private boolean hasActiveGatePass;
        private String passNumber;
        private String validUntil;
        private String passStatus;
        private String qrToken;
    }
}

