package com.example.web_adventure.user_management.employee.repository;

import com.example.web_adventure.old.model.Department;
import com.example.web_adventure.old.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    
    List<Employee> findByDepartment(Department department);
    
    List<Employee> findByStatus(String status);
    
    Optional<Employee> findByUserId(Long userId);
    
    @Query("SELECT e FROM Employee e WHERE LOWER(e.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(e.lastName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Employee> searchByName(String keyword);
    
    @Query("SELECT e FROM Employee e JOIN e.department d WHERE d.id = :departmentId")
    List<Employee> findByDepartmentId(Long departmentId);
}