package com.example.web_adventure.user_management.employee.service.impl;

import com.example.web_adventure.exception.ResourceNotFoundException;
import com.example.web_adventure.user_management.department.entity.Department;
import com.example.web_adventure.user_management.department.repository.DepartmentRepository;
import com.example.web_adventure.user_management.employee.dto.EmployeeDTO;
import com.example.web_adventure.user_management.employee.entity.Employee;
import com.example.web_adventure.user_management.employee.entity.EmployeeStatus;
import com.example.web_adventure.user_management.employee.repository.EmployeeRepository;
import com.example.web_adventure.user_management.employee.service.EmployeeService;
import com.example.web_adventure.user_management.user.entity.User;
import com.example.web_adventure.user_management.user.repository.UserRepository;
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
    public EmployeeDTO createEmployee(EmployeeDTO dto) {
        Employee employee = convertToEntity(dto);
        employee.setCreatedBy(getUserOrNull(dto.getCreatedById()));
        employee.setUpdatedBy(getUserOrNull(dto.getUpdatedById()));
        Employee saved = employeeRepository.save(employee);
        return convertToDTO(saved);
    }

    @Override
    @Transactional
    public EmployeeDTO updateEmployee(Long id, EmployeeDTO dto) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id));

        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setPosition(dto.getPosition());
        employee.setHireDate(dto.getHireDate());
        employee.setStatus(EmployeeStatus.valueOf(dto.getStatus()));
        employee.setDeleted(dto.isDeleted());

        if (dto.getDepartmentId() != null) {
            Department department = departmentRepository.findById(dto.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department", "id", dto.getDepartmentId()));
            employee.setDepartment(department);
        }

        if (dto.getUpdatedById() != null) {
            employee.setUpdatedBy(getUserOrNull(dto.getUpdatedById()));
        }

        return convertToDTO(employeeRepository.save(employee));
    }

    @Override
    @Transactional
    public boolean deleteEmployee(Long id) {
        return employeeRepository.findById(id).map(emp -> {
            emp.setDeleted(true);
            employeeRepository.save(emp);
            return true;
        }).orElse(false);
    }

    @Override
    public List<EmployeeDTO> getEmployeesByDepartment(Long departmentId) {
        if (!departmentRepository.existsById(departmentId)) {
            throw new ResourceNotFoundException("Department", "id", departmentId);
        }

        return employeeRepository.findByDepartment_DepartmentId(departmentId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // ================================
    // Conversion methods
    // ================================

    private EmployeeDTO convertToDTO(Employee employee) {
        User user = employee.getUser();
        Department department = employee.getDepartment();

        return EmployeeDTO.builder()
                .employeeId(employee.getEmployeeId())
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .position(employee.getPosition())
                .departmentId(department.getDepartmentId())
                .departmentName(department.getDepartmentName())
                .hireDate(employee.getHireDate())
                .status(employee.getStatus().name())
                .createdById(getUserIdSafe(employee.getCreatedBy()))
                .createdByUsername(getUsernameSafe(employee.getCreatedBy()))
                .updatedById(getUserIdSafe(employee.getUpdatedBy()))
                .updatedByUsername(getUsernameSafe(employee.getUpdatedBy()))
                .createdAt(employee.getCreatedAt())
                .updatedAt(employee.getUpdatedAt())
                .isDeleted(employee.isDeleted())
                .build();
    }

    private Employee convertToEntity(EmployeeDTO dto) {
        Employee employee = new Employee();
        employee.setEmployeeId(dto.getEmployeeId());
        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setPosition(dto.getPosition());
        employee.setHireDate(dto.getHireDate() != null ? dto.getHireDate() : LocalDate.now());
        employee.setStatus(EmployeeStatus.valueOf(dto.getStatus()));
        employee.setDeleted(dto.isDeleted());

        if (dto.getUserId() != null) {
            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", dto.getUserId()));
            employee.setUser(user);
        }

        if (dto.getDepartmentId() != null) {
            Department department = departmentRepository.findById(dto.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department", "id", dto.getDepartmentId()));
            employee.setDepartment(department);
        }

        return employee;
    }

    // ================================
    // Utility methods
    // ================================

    private User getUserOrNull(Long userId) {
        return userId != null ? userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId)) : null;
    }

    private Long getUserIdSafe(User user) {
        return user != null ? user.getUserId() : null;
    }

    private String getUsernameSafe(User user) {
        return user != null ? user.getUsername() : null;
    }
}
