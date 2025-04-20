package com.example.web_adventure.roadmap_management.roadmap_area_course.entity;

import java.io.Serializable;
import lombok.*;

/**
 * Composite key for roadmap_area_courses.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RoadmapAreaCourseId implements Serializable {
    private Long roadmap;
    private Long area;
    private Long course;
}
