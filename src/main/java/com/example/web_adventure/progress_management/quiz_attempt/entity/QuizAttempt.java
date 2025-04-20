package com.example.web_adventure.progress_management.quiz_attempt.entity;

import com.example.web_adventure.course_management.assess.quizz.entity.Quiz;
import com.example.web_adventure.user_management.employee.entity.Employee;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

/**
 * A user's attempt at taking a quiz.
 */
@Entity
@Table(name = "quiz_attempts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizAttempt {

    @Id
    @Column(name = "attempt_id", nullable = false, updatable = false)
    private Long attemptId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "attempt_number", nullable = false)
    private Integer attemptNumber;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "time_spent_seconds", nullable = false)
    private Integer timeSpentSeconds;

    @Column
    private java.math.BigDecimal score;

    @Column(name = "max_score")
    private java.math.BigDecimal maxScore;

    @Column
    private Boolean passed;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
