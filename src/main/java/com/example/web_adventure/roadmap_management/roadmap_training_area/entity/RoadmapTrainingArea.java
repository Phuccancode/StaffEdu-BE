package com.example.web_adventure.roadmap_management.roadmap_training_area.entity;

import com.example.web_adventure.roadmap_management.user_roadmap.entity.UserRoadmap;
import com.example.web_adventure.training_area_management.training_area.entity.TrainingArea;
import com.example.web_adventure.user_management.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Associates training areas to a roadmap with scheduling info.
 */
@Entity
@Table(name = "roadmap_training_areas")
@IdClass(RoadmapTrainingAreaId.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoadmapTrainingArea {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roadmap_id", nullable = false)
    private UserRoadmap roadmap;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_id", nullable = false)
    private TrainingArea area;

    @Column(nullable = false)
    private Integer sequence;

    @Column(name = "target_start_date")
    private LocalDate targetStartDate;

    @Column(name = "actual_start_date")
    private LocalDate actualStartDate;

    @Column(name = "target_end_date")
    private LocalDate targetEndDate;

    @Column(name = "actual_end_date")
    private LocalDate actualEndDate;

    @Column(name = "allocated_hours", nullable = false)
    private Integer allocatedHours;

    @Column(nullable = false)
    private Short weight;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "roadmap_area_status_enum")
    private RoadmapAreaStatus status;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    private User updatedBy;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
