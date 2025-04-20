package com.example.web_adventure.progress_management.assignment_submission.entity;

import com.example.web_adventure.course_management.assess.assignment.entity.Assignment;
import com.example.web_adventure.user_management.employee.entity.Employee;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

/**
 * Submission record for an assignment.
 */
@Entity
@Table(name = "assignment_submissions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssignmentSubmission {

    @Id
    @Column(name = "submission_id", nullable = false, updatable = false)
    private Long submissionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_id", nullable = false)
    private Assignment assignment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "submission_version", nullable = false)
    private Integer submissionVersion;

    @CreationTimestamp
    @Column(name = "submitted_at", nullable = false, updatable = false)
    private LocalDateTime submittedAt;

    @Column(name = "file_url", length = 500)
    private String fileUrl;

    @Column(name = "text_submission", columnDefinition = "TEXT")
    private String textSubmission;

    @Column(name = "time_spent_seconds", nullable = false)
    private Integer timeSpentSeconds;

    @Column
    private java.math.BigDecimal grade;

    @Column(columnDefinition = "TEXT")
    private String feedback;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "submission_status_enum")
    private SubmissionStatus status;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
