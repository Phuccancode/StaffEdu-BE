package com.example.web_adventure.user_management.employee.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for Employee entity.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeDTO {

    private Long employeeId;

    // User info (linked to User entity)
    private Long userId;
    private String username;
    private String email;

    // Basic info
    private String firstName;
    private String lastName;
    private String position;

    // Department info (linked to Department entity)
    private Long departmentId;
    private String departmentName;

    private LocalDate hireDate;
    private String status; // enum: 'active', 'inactive', etc.

    // Audit info
    private Long createdById;
    private String createdByUsername;

    private Long updatedById;
    private String updatedByUsername;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private boolean isDeleted;
}
