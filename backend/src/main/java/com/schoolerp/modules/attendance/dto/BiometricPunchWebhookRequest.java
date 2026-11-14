package com.schoolerp.modules.attendance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BiometricPunchWebhookRequest {
    private String deviceSerialNumber; // e.g. eSSL-GATE-01
    private String rawIpAddress;
    private List<BiometricPunchPayloadItemDTO> punches;
}

