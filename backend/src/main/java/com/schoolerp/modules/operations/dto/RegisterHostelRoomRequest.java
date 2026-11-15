package com.schoolerp.modules.operations.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterHostelRoomRequest {
    @NotBlank
    private String blockName;

    @Min(1)
    private int floorNumber;

    @NotBlank
    private String roomNumber;

    private String roomType; // AC, NON_AC

    @Min(1)
    private int totalBeds;

    private BigDecimal annualFee;
}

