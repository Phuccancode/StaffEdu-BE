package com.example.web_adventure.user_management.employee.repository;

import com.example.web_adventure.user_management.employee.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByDepartment_DepartmentId(Long departmentId);
}