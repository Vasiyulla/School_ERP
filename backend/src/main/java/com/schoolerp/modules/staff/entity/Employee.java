package com.schoolerp.modules.staff.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "employees", schema = "staff")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false, unique = true)
    private String employeeCode; // e.g. EMP-2024-0042

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String email;

    private String phone;

    @Column(nullable = false)
    private String department; // e.g. Science, Mathematics, Administration, Sports

    @Column(nullable = false)
    private String designation; // e.g. PGT Physics, TGT English, Lab Assistant, Accountant

    @Builder.Default
    private String employmentType = "FULL_TIME"; // FULL_TIME, CONTRACT, VISITING

    private LocalDate joiningDate;

    private String panNumber;

    private String bankAccountNumber;

    @Builder.Default
    private String status = "ACTIVE"; // ACTIVE, ON_LEAVE, RESIGNED, RETIRED
}

