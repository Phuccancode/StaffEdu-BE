package com.example.web_adventure.course_management.metadata.course_outcomes_training.entity;

import com.example.web_adventure.course_management.metadata.course.entity.Course;
import com.example.web_adventure.training_area_management.training_area.entity.TrainingArea;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Learning objectives or outcomes associated with a course.
 */
@Entity
@Table(name = "course_outcomes_training")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseOutcomeTraining {

    @Id
    @Column(name = "outcome_id", nullable = false, updatable = false)
    private Long outcomeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_id", nullable = false)
    private TrainingArea area;

    @Column(columnDefinition = "TEXT")
    private String description;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
