package com.example.web_adventure.course_management.content.content.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Generic content entity representing various types of course materials.
 */
@Entity
@Table(name = "content")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Content {

    @Id
    @Column(name = "content_id", nullable = false, updatable = false)
    private Long contentId;

    @Column(length = 255, nullable = false)
    private String title;

    @Column(length = 50)
    private String type;

    @Column(name = "content_url", length = 500)
    private String contentUrl;

    @Column(name = "content_text", columnDefinition = "TEXT")
    private String contentText;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
