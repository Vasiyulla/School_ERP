package com.schoolerp.modules.staff.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "departments", schema = "staff")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String departmentCode; // FINANCE, ACADEMICS, ADMINISTRATION, OPERATIONS, SPORTS

    @Column(nullable = false)
    private String departmentName; // Finance & Accounts, Department of Physical Sciences

    private String hodEmployeeName;

    @Column(nullable = false)
    @Builder.Default
    private boolean isActive = true;
}

