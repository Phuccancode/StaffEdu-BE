package com.example.web_adventure.course_management.metadata.course.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseDTO {
    private Long id;
    private String title;
    private String description;
    private Integer duration;
    private String contentDescription;
    private String learningOutcomes;
    private Long courseDomainId;
    private String courseDomainName;
    private Long statusId;
    private String statusName;
    private String thumbnailUrl;
    private Long instructorId;
    private String instructorName;
    private Set<Long> prerequisiteIds;
}