package com.example.web_adventure.progress_management.grade_history.entity;

import com.example.web_adventure.user_management.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * History of grade changes for assignments and quizzes.
 */
@Entity
@Table(name = "grade_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GradeHistory {

    @Id
    @Column(name = "history_id", nullable = false, updatable = false)
    private Long historyId;

    @Column(name = "related_type", length = 20, nullable = false)
    private String relatedType;

    @Column(name = "related_id", nullable = false)
    private Long relatedId;

    @Column(name = "previous_grade")
    private BigDecimal previousGrade;

    @Column(name = "new_grade")
    private BigDecimal newGrade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "changed_by")
    private User changedBy;

    @CreationTimestamp
    @Column(name = "changed_at", nullable = false, updatable = false)
    private LocalDateTime changedAt;
}
