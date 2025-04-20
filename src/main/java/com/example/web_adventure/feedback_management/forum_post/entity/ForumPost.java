package com.example.web_adventure.feedback_management.forum_post.entity;

import com.example.web_adventure.feedback_management.forum_thread.entity.ForumThread;
import com.example.web_adventure.user_management.employee.entity.Employee;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Post within a forum thread.
 */
@Entity
@Table(name = "forum_posts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ForumPost {

    @Id
    @Column(name = "post_id", nullable = false, updatable = false)
    private Long postId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "thread_id", nullable = false)
    private ForumThread thread;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_post_id")
    private ForumPost parentPost;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
