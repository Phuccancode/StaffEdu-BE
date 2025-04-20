package com.example.web_adventure.course_management.metadata.course.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for Course.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseDTO {

    private Long courseId;

    private String courseCode;

    private String title;

    private String description;

    private String contentDescription;

    private String language;

    private LocalDate startDate;

    private LocalDate endDate;

    private LocalDateTime enrollmentStart;

    private LocalDateTime enrollmentEnd;

    private Integer maxEnrollment;

    private BigDecimal price;

    private String currency;

    private Integer durationWeeks;

    private Integer durationHours;

    private Integer expectedTimePerWeek;

    private String status;

    private Long instructorId;
    private String instructorName;

    private String thumbnailUrl;

    private String syllabusUrl;

    private BigDecimal ratingAvg;

    private Integer ratingCount;

    private Integer totalEnrolled;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
