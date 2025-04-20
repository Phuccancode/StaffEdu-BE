package com.example.web_adventure.roadmap_management.roadmap_area_course.entity;

import com.example.web_adventure.course_management.metadata.course.entity.Course;
import com.example.web_adventure.progress_management.enrollment.entity.Enrollment;
import com.example.web_adventure.progress_management.enrollment.entity.EnrollmentStatus;
import com.example.web_adventure.roadmap_management.user_roadmap.entity.UserRoadmap;
import com.example.web_adventure.training_area_management.training_area.entity.TrainingArea;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Links courses within a roadmap area, tracking progress.
 */
@Entity
@Table(name = "roadmap_area_courses")
@IdClass(RoadmapAreaCourseId.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoadmapAreaCourse {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roadmap_id", nullable = false)
    private UserRoadmap roadmap;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_id", nullable = false)
    private TrainingArea area;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollment_id", nullable = false)
    private Enrollment enrollment;

    @CreationTimestamp
    @Column(name = "selected_date", nullable = false, updatable = false)
    private LocalDateTime selectedDate;

    @Column(name = "allocated_hours", nullable = false)
    private Integer allocatedHours;

    @Column(name = "recommended_order")
    private Integer recommendedOrder;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "enrollment_status_enum")
    private EnrollmentStatus status;

    @Column(name = "progress_percent", nullable = false)
    private Short progressPercent;

    @Column(name = "actual_start_date")
    private LocalDate actualStartDate;

    @Column(name = "actual_end_date")
    private LocalDate actualEndDate;

    @Column(columnDefinition = "TEXT")
    private String feedback;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
