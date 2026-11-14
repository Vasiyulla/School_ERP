package com.schoolerp.modules.academics.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AutoGenerateVirtualSessionsRequest {
    private LocalDate sessionDate;
    private String className;
    private String sectionName;
    private String meetingProvider; // JITSI_WEBRTC (Default), ZOOM_EDUCATION, GOOGLE_MEET
    private List<PeriodScheduleItemDTO> periods;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PeriodScheduleItemDTO {
        private int periodNumber;
        private String subjectName;
        private String teacherName;
        private String startTime;
        private String endTime;
    }
}

