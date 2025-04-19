package com.example.web_adventure.old.service.impl;

import com.example.web_adventure.old.dto.EmployeeDTO;
import com.example.web_adventure.exception.ResourceNotFoundException;
import com.example.web_adventure.old.model.Department;
import com.example.web_adventure.old.model.Employee;
import com.example.web_adventure.old.model.User;
import com.example.web_adventure.old.repository.DepartmentRepository;
import com.example.web_adventure.old.repository.EmployeeRepository;
import com.example.web_adventure.old.repository.UserRepository;
import com.example.web_adventure.old.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    
    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, 
                             DepartmentRepository departmentRepository,
                             UserRepository userRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
    }
    
    @Override
    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public Optional<EmployeeDTO> getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .map(this::convertToDTO);
    }
    
    @Override
    @Transactional
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        Employee employee = convertToEntity(employeeDTO);
        Employee savedEmployee = employeeRepository.save(employee);
        return convertToDTO(savedEmployee);
    }
    
    @Override
    @Transactional
    public EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO) {
        return employeeRepository.findById(id)
                .map(employee -> {
                    // Update fields
                    employee.setFirstName(employeeDTO.getFirstName());
                    employee.setLastName(employeeDTO.getLastName());
                    employee.setPosition(employeeDTO.getPosition());
                    employee.setHireDate(employeeDTO.getHireDate());
                    employee.setStatus(employeeDTO.getStatus());
                    
                    // Update department if changed
                    if (employeeDTO.getDepartmentId() != null) {
                        departmentRepository.findById(employeeDTO.getDepartmentId())
                                .ifPresent(employee::setDepartment);
                    }
                    
                    Employee updatedEmployee = employeeRepository.save(employee);
                    return convertToDTO(updatedEmployee);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id));
    }
    
    @Override
    @Transactional
    public boolean deleteEmployee(Long id) {
        if (employeeRepository.existsById(id)) {
            employeeRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    @Override
    public List<EmployeeDTO> searchEmployees(String keyword) {
        return employeeRepository.searchByName(keyword).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<EmployeeDTO> getEmployeesByDepartment(Long departmentId) {
        // Kiểm tra sự tồn tại của department trước
        if (!departmentRepository.existsById(departmentId)) {
            throw new ResourceNotFoundException("Department", "id", departmentId);
        }
        
        return employeeRepository.findByDepartmentId(departmentId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    private EmployeeDTO convertToDTO(Employee employee) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(employee.getId());
        dto.setUserId(employee.getUser().getId());
        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setDepartmentId(employee.getDepartment().getId());
        dto.setDepartmentName(employee.getDepartment().getName());
        dto.setPosition(employee.getPosition());
        dto.setHireDate(employee.getHireDate());
        dto.setStatus(employee.getStatus());
        dto.setEmail(employee.getUser().getEmail());
        return dto;
    }
    
    private Employee convertToEntity(EmployeeDTO dto) {
        Employee employee = new Employee();
        employee.setId(dto.getId());
        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setPosition(dto.getPosition());
        employee.setHireDate(dto.getHireDate() != null ? dto.getHireDate() : LocalDate.now());
        employee.setStatus(dto.getStatus());
        
        // Set department
        if (dto.getDepartmentId() != null) {
            Department department = departmentRepository.findById(dto.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department", "id", dto.getDepartmentId()));
            employee.setDepartment(department);
        }
        
        // Set user
        if (dto.getUserId() != null) {
            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", dto.getUserId()));
            employee.setUser(user);
        }
        
        return employee;
    }
}