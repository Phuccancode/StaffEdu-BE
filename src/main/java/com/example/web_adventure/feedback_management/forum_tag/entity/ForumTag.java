package com.example.web_adventure.feedback_management.forum_tag.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Tag for categorizing threads.
 */
@Entity
@Table(name = "forum_tags")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ForumTag {

    @Id
    @Column(name = "tag_id", nullable = false, updatable = false)
    private Long tagId;

    @Column(length = 100, nullable = false, unique = true)
    private String name;
}
