package com.example.web_adventure.roadmap_management.roadmap_share.entity;

import com.example.web_adventure.roadmap_management.user_roadmap.entity.UserRoadmap;
import com.example.web_adventure.user_management.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Sharing permissions for a roadmap.
 */
@Entity
@Table(name = "roadmap_shares")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoadmapShare {

    @Id
    @Column(name = "share_id", nullable = false, updatable = false)
    private Long shareId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roadmap_id", nullable = false)
    private UserRoadmap roadmap;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(length = 20, nullable = false)
    private String permission;

    @CreationTimestamp
    @Column(name = "shared_at", nullable = false, updatable = false)
    private LocalDateTime sharedAt;
}
