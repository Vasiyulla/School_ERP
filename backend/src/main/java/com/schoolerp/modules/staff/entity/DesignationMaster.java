package com.schoolerp.modules.staff.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "designations", schema = "staff")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DesignationMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String designationCode; // e.g. HEAD_OF_FINANCE, ACCOUNTANT, CASHIER, HOD_SCIENCE

    @Column(nullable = false)
    private String title; // e.g. Head of Finance & Accounts, Senior Bursar

    @Column(nullable = false)
    private String department; // Finance, Academics, Administration, Operations, Sports

    @Column(nullable = false)
    @Builder.Default
    private Integer hierarchyTier = 4; // 1 = Executive/Director, 2 = Principal, 3 = HOD/Dean, 4 = Senior, 5 = Junior

    private String reportsToDesignationCode; // e.g. HEAD_OF_FINANCE, PRINCIPAL

    @Column(nullable = false)
    @Builder.Default
    private String baseRole = "ROLE_STAFF"; // ROLE_ACCOUNTANT, ROLE_TEACHER, ROLE_PRINCIPAL, ROLE_ADMIN, ROLE_STAFF

    @Column(length = 2000)
    @Builder.Default
    private String actionPermissionsJson = "[]";

    @Column(nullable = false, precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal financialApprovalLimit = BigDecimal.ZERO; // e.g. 500000.00 for Head of Finance

    private String defaultCpcLevel; // LEVEL_10, LEVEL_8, LEVEL_7, LEVEL_6

    @Column(nullable = false)
    @Builder.Default
    private boolean isActive = true;

    @Column(nullable = false)
    @Builder.Default
    private boolean isSystemStandard = false;
}

