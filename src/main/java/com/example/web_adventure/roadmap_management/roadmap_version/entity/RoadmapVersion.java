package com.example.web_adventure.roadmap_management.roadmap_version.entity;

import com.example.web_adventure.roadmap_management.user_roadmap.entity.UserRoadmap;
import com.example.web_adventure.user_management.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Versioned snapshots of a roadmap.
 */
@Entity
@Table(name = "roadmap_versions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoadmapVersion {

    @Id
    @Column(name = "version_id", nullable = false, updatable = false)
    private Long versionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roadmap_id", nullable = false)
    private UserRoadmap roadmap;

    @Column(name = "version_number", nullable = false)
    private Integer versionNumber;

    @Column(columnDefinition = "jsonb", nullable = false)
    private String snapshot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}