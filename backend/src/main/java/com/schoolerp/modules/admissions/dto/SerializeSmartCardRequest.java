package com.schoolerp.modules.admissions.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SerializeSmartCardRequest {

    @NotBlank(message = "Smart card RFID UID is required")
    private String smartCardRfidUid;

    private BigDecimal addBalance;
}
