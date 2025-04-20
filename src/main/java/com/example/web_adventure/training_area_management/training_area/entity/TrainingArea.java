package com.example.web_adventure.training_area_management.training_area.entity;

import com.example.web_adventure.user_management.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Represents a training area within the system.
 */
@Entity
@Table(name = "training_areas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainingArea {

    @Id
    @Column(name = "area_id", nullable = false, updatable = false)
    private Long areaId;

    @Column(name = "area_code", length = 20, nullable = false, unique = true)
    private String areaCode;

    @Column(name = "area_name", length = 255, nullable = false)
    private String areaName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
