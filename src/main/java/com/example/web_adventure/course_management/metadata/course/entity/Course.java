package com.example.web_adventure.course_management.metadata.course.entity;

import com.example.web_adventure.course_management.metadata.course_status.entity.CourseStatus;
import com.example.web_adventure.user_management.instructor.entity.Instructor;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Represents a course offering.
 */
@Entity
@Table(name = "courses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {

    @Id
    @Column(name = "course_id", nullable = false, updatable = false)
    private Long courseId;

    @Column(name = "course_code", length = 50, nullable = false, unique = true)
    private String courseCode;

    @Column(length = 255, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "content_description", columnDefinition = "TEXT")
    private String contentDescription;

    @Column(length = 10, nullable = false)
    private String language;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "enrollment_start")
    private LocalDateTime enrollmentStart;

    @Column(name = "enrollment_end")
    private LocalDateTime enrollmentEnd;

    @Column(name = "max_enrollment", nullable = false)
    private Integer maxEnrollment;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(length = 3, nullable = false)
    private String currency;

    private Integer durationWeeks;

    private Integer durationHours;

    private Integer expectedTimePerWeek;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id", nullable = false)
    private CourseStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instructor_id", nullable = false)
    private Instructor instructor;

    @Column(name = "thumbnail_url", length = 500)
    private String thumbnailUrl;

    @Column(name = "syllabus_url", length = 500)
    private String syllabusUrl;

    @Column(name = "rating_avg", precision = 3, scale = 2)
    private BigDecimal ratingAvg;

    @Column(name = "rating_count", nullable = false)
    private Integer ratingCount;

    @Column(name = "total_enrolled", nullable = false)
    private Integer totalEnrolled;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
