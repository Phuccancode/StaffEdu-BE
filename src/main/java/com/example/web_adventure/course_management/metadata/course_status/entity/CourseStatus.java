package com.example.web_adventure.course_management.metadata.course_status.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Status values for courses.
 */
@Entity
@Table(name = "course_statuses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseStatus {

    @Id
    @Column(name = "status_id", nullable = false, updatable = false)
    private Long statusId;

    @Column(name = "status_name", length = 50, nullable = false, unique = true)
    private String statusName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
