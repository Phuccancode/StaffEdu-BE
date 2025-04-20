package com.example.web_adventure.course_management.assess.question_option.entity;

import com.example.web_adventure.course_management.assess.quiz_question.entity.QuizQuestion;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Options for a specific quiz question.
 */
@Entity
@Table(name = "question_options")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionOption {

    @Id
    @Column(name = "option_id", nullable = false, updatable = false)
    private Long optionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private QuizQuestion question;

    @Column(name = "option_text", columnDefinition = "TEXT")
    private String optionText;

    @Column(name = "is_correct", nullable = false)
    private boolean isCorrect;

    @Column(name = "order_index")
    private Integer orderIndex;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
