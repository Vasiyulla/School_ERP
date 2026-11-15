package com.schoolerp.modules.operations.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HostelRoomDTO {
    private UUID id;
    private String blockName;
    private int floorNumber;
    private String roomNumber;
    private String roomType;
    private int totalBeds;
    private int occupiedBeds;
    private int availableBeds;
    private BigDecimal annualFee;
}

