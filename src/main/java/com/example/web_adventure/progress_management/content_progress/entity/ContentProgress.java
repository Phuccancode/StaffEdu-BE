package com.example.web_adventure.progress_management.content_progress.entity;

import com.example.web_adventure.course_management.content.content.entity.Content;
import com.example.web_adventure.progress_management.enrollment.entity.Enrollment;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Progress record per content item within an enrollment.
 */
@Entity
@Table(name = "content_progress", uniqueConstraints = @UniqueConstraint(columnNames = {"enrollment_id","content_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContentProgress {

    @Id
    @Column(name = "progress_id", nullable = false, updatable = false)
    private Long progressId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollment_id", nullable = false)
    private Enrollment enrollment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id", nullable = false)
    private Content content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "progress_status_enum")
    private ProgressStatus status;

    @Column(name = "percent_complete", nullable = false)
    private Short percentComplete;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "time_spent_seconds", nullable = false)
    private Integer timeSpentSeconds;

    @Column(name = "last_accessed_at")
    private LocalDateTime lastAccessedAt;

    @Column(name = "last_activity_at")
    private LocalDateTime lastActivityAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
