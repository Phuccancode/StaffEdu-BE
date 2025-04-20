package com.example.web_adventure.user_management.employee_hierarchy.entity;

import com.example.web_adventure.user_management.employee.entity.Employee;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Represents the reporting hierarchy between managers and employees.
 */
@Entity
@Table(
        name = "employee_hierarchy",
        uniqueConstraints = @UniqueConstraint(columnNames = {"manager_id", "employee_id"})
)
@Check(constraints = "manager_id <> employee_id")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeHierarchy {

    @Id
    @Column(name = "hierarchy_id", nullable = false, updatable = false)
    private Long hierarchyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", nullable = false)
    private Employee manager;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}

