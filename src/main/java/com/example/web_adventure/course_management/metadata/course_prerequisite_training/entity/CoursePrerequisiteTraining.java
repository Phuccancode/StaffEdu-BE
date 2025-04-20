package com.example.web_adventure.course_management.metadata.course_prerequisite_training.entity;

import com.example.web_adventure.course_management.metadata.course.entity.Course;
import com.example.web_adventure.training_area_management.training_area.entity.TrainingArea;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Prerequisite mapping between courses and training areas.
 */
@Entity
@Table(name = "course_prerequisite_training")
@IdClass(CoursePrerequisiteTrainingId.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CoursePrerequisiteTraining {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_id", nullable = false)
    private TrainingArea area;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
