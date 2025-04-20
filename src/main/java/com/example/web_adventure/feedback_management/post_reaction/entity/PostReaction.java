package com.example.web_adventure.feedback_management.post_reaction.entity;

import com.example.web_adventure.feedback_management.forum_post.entity.ForumPost;
import com.example.web_adventure.user_management.employee.entity.Employee;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Reaction on a forum post.
 */
@Entity
@Table(name = "post_reactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostReaction {

    @Id
    @Column(name = "reaction_id", nullable = false, updatable = false)
    private Long reactionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private ForumPost post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Enumerated(EnumType.STRING)
    @Column(name = "reaction_type", nullable = false, columnDefinition = "reaction_type_enum")
    private ReactionType reactionType;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
