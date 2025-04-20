package com.example.web_adventure.course_management.metadata.course_prerequisite_training.entity;

import java.io.Serializable;
import lombok.*;

/**
 * Composite primary key for CoursePrerequisiteTraining.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CoursePrerequisiteTrainingId implements Serializable {
    private Long course;
    private Long area;
}
