package com.example.web_adventure.course_management.content.module_items.entity;

import com.example.web_adventure.course_management.assess.assignment.entity.Assignment;
import com.example.web_adventure.course_management.assess.quizz.entity.Quiz;
import com.example.web_adventure.course_management.content.content.entity.Content;
import com.example.web_adventure.course_management.content.module.entity.Module;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Links module elements (content, quiz, assignment) in sequence.
 */
@Entity
@Table(name = "module_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModuleItem {

    @Id
    @Column(name = "item_id", nullable = false, updatable = false)
    private Long itemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id", nullable = false)
    private Module module;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id")
    private Content content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_id")
    private Assignment assignment;

    @Column(name = "order_index")
    private Integer orderIndex;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
