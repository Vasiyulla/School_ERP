package com.schoolerp.modules.operations.dto;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HostelRosterDTO {
    private String blockName;
    private int totalRooms;
    private int totalBeds;
    private int totalOccupied;
    private int totalAvailable;
    private List<HostelAllocationDTO> residents;
}

