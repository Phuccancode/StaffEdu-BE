package com.example.web_adventure.course_management.assess.assignment.entity;

import com.example.web_adventure.course_management.metadata.course.entity.Course;
import com.example.web_adventure.course_management.content.module.entity.Module;
import com.example.web_adventure.user_management.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Represents an assignment within a course module.
 */
@Entity
@Table(name = "assignments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Assignment {

    @Id
    @Column(name = "assignment_id", nullable = false, updatable = false)
    private Long assignmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id")
    private Module module;

    @Column(length = 255, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @CreationTimestamp
    @Column(name = "assigned_date", nullable = false, updatable = false)
    private LocalDateTime assignedDate;

    @Column(name = "available_from")
    private LocalDateTime availableFrom;

    @Column(nullable = false)
    private LocalDateTime deadline;

    @Column(name = "late_deadline")
    private LocalDateTime lateDeadline;

    @Column(name = "grace_period_minutes", nullable = false)
    private Integer gracePeriodMinutes;

    @Column(name = "time_limit_seconds")
    private Integer timeLimitSeconds;

    @Column(name = "max_attempts", nullable = false)
    private Integer maxAttempts;

    @Enumerated(EnumType.STRING)
    @Column(name = "submission_type", nullable = false, columnDefinition = "assignment_type_enum")
    private AssignmentType submissionType;

    @Column(name = "allow_late_submission", nullable = false)
    private boolean allowLateSubmission;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "assignment_status_enum")
    private AssignmentStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    private User updatedBy;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
