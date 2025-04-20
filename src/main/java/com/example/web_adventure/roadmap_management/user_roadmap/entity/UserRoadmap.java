package com.example.web_adventure.roadmap_management.user_roadmap.entity;

import com.example.web_adventure.user_management.employee.entity.Employee;
import com.example.web_adventure.user_management.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Represents a user-specific learning roadmap.
 */
@Entity
@Table(name = "user_roadmaps")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRoadmap {

    @Id
    @Column(name = "roadmap_id", nullable = false, updatable = false)
    private Long roadmapId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(length = 255, nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 10, nullable = false)
    private String priority;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by")
    private User approvedBy;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "actual_start_date")
    private LocalDate actualStartDate;

    @Column(name = "target_end_date")
    private LocalDate targetEndDate;

    @Column(name = "actual_end_date")
    private LocalDate actualEndDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "roadmap_status_enum")
    private RoadmapStatus status;

    @Column(name = "is_current", nullable = false)
    private boolean current;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    private User updatedBy;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
