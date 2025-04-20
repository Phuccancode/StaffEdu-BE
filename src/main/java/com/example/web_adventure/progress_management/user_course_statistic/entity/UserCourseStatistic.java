package com.example.web_adventure.progress_management.user_course_statistic.entity;

import com.example.web_adventure.course_management.metadata.course.entity.Course;
import com.example.web_adventure.progress_management.enrollment.entity.Enrollment;
import com.example.web_adventure.user_management.employee.entity.Employee;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

/**
 * Aggregated statistics for a user's course engagement.
 */
@Entity
@Table(name = "user_course_statistics")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCourseStatistic {

    @Id
    @Column(name = "stat_id", nullable = false, updatable = false)
    private Long statId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollment_id", nullable = false)
    private Enrollment enrollment;

    @Column(name = "modules_total", nullable = false)
    private Integer modulesTotal;

    @Column(name = "modules_completed", nullable = false)
    private Integer modulesCompleted;

    @Column(name = "contents_total", nullable = false)
    private Integer contentsTotal;

    @Column(name = "contents_completed", nullable = false)
    private Integer contentsCompleted;

    @Column(name = "avg_quiz_score")
    private java.math.BigDecimal avgQuizScore;

    @Column(name = "quiz_attempts_count", nullable = false)
    private Integer quizAttemptsCount;

    @Column(name = "avg_assignment_score")
    private java.math.BigDecimal avgAssignmentScore;

    @Column(name = "assignments_submitted", nullable = false)
    private Integer assignmentsSubmitted;

    @Column(name = "overall_progress", nullable = false)
    private Short overallProgress;

    @Column(name = "last_activity_at")
    private LocalDateTime lastActivityAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
