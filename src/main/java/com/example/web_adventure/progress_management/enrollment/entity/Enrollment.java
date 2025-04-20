package com.example.web_adventure.progress_management.enrollment.entity;

import com.example.web_adventure.course_management.metadata.course.entity.Course;
import com.example.web_adventure.user_management.employee.entity.Employee;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Enrollment of an employee in a course.
 */
@Entity
@Table(name = "enrollments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Enrollment {

    @Id
    @Column(name = "enrollment_id", nullable = false, updatable = false)
    private Long enrollmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @CreationTimestamp
    @Column(name = "enrollment_date", nullable = false, updatable = false)
    private LocalDateTime enrollmentDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "enrollment_status_enum")
    private EnrollmentStatus status;

    @Column(name = "progress_percent", nullable = false)
    private Short progressPercent;

    @Column(name = "time_spent_seconds", nullable = false)
    private Integer timeSpentSeconds;

    @Column(name = "last_accessed_at")
    private LocalDateTime lastAccessedAt;

    @Column(name = "completion_date")
    private LocalDateTime completionDate;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
