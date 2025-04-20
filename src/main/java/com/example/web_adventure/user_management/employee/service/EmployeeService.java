package com.example.web_adventure.user_management.employee.service;

import com.example.web_adventure.user_management.employee.dto.EmployeeDTO;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    
    List<EmployeeDTO> getAllEmployees();
    
    Optional<EmployeeDTO> getEmployeeById(Long id);
    
    EmployeeDTO createEmployee(EmployeeDTO employeeDTO);
    
    EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO);
    
    boolean deleteEmployee(Long id);
    
    List<EmployeeDTO> getEmployeesByDepartment(Long departmentId);
}