package com.example.web_adventure.training_area_management.training_area_hierarchy.entity;

import com.example.web_adventure.training_area_management.training_area.entity.TrainingArea;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Represents the parent-child relationship between training areas.
 */
@Entity
@Table(name = "training_area_hierarchy")
@IdClass(TrainingAreaHierarchyId.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainingAreaHierarchy {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_area_id", nullable = false)
    private TrainingArea parentArea;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_area_id", nullable = false)
    private TrainingArea childArea;

    private Integer sequence;

    @Column(columnDefinition = "TEXT")
    private String description;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
