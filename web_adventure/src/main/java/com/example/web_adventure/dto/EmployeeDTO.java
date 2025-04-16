package com.example.web_adventure.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {
    private Long id;
    private Long userId;
    private String firstName;
    private String lastName;
    private Long departmentId;
    private String departmentName;
    private String position;
    private LocalDate hireDate;
    private String status;
    private String email;
}