package com.example.web_adventure.training_area_management.training_area_content.entity;

import com.example.web_adventure.training_area_management.training_area.entity.TrainingArea;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Content within a training area.
 */
@Entity
@Table(name = "training_area_contents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainingAreaContent {

    @Id
    @Column(name = "content_id", nullable = false, updatable = false)
    private Long contentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_id", nullable = false)
    private TrainingArea area;

    @Column(name = "content_title", length = 255, nullable = false)
    private String contentTitle;

    @Column(name = "content_description", columnDefinition = "TEXT")
    private String contentDescription;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
