package com.example.web_adventure.feedback_management.forum_thread.entity;

import com.example.web_adventure.feedback_management.course_forum.entity.CourseForum;
import com.example.web_adventure.user_management.employee.entity.Employee;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Thread within a course forum.
 */
@Entity
@Table(name = "forum_threads")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ForumThread {

    @Id
    @Column(name = "thread_id", nullable = false, updatable = false)
    private Long threadId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "forum_id", nullable = false)
    private CourseForum forum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(length = 255, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(nullable = false)
    private short rating;

    @Column(name = "is_sticky", nullable = false)
    private boolean sticky;

    @Column(name = "views_count", nullable = false)
    private int viewsCount;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
