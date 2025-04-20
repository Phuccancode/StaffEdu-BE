package com.example.web_adventure.progress_management.quiz_answers.entity;

import com.example.web_adventure.course_management.assess.question_option.entity.QuestionOption;
import com.example.web_adventure.course_management.assess.quiz_question.entity.QuizQuestion;
import com.example.web_adventure.progress_management.quiz_attempt.entity.QuizAttempt;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

/**
 * An answer provided in a quiz attempt.
 */
@Entity
@Table(name = "quiz_answers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizAnswer {

    @Id
    @Column(name = "answer_id", nullable = false, updatable = false)
    private Long answerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attempt_id", nullable = false)
    private QuizAttempt attempt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private QuizQuestion question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "selected_option")
    private QuestionOption selectedOption;

    @Column(name = "answer_text", columnDefinition = "TEXT")
    private String answerText;

    @Column(name = "is_correct")
    private Boolean isCorrect;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
