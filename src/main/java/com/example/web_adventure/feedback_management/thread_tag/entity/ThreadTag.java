package com.example.web_adventure.feedback_management.thread_tag.entity;

import com.example.web_adventure.feedback_management.forum_tag.entity.ForumTag;
import com.example.web_adventure.feedback_management.forum_thread.entity.ForumThread;
import jakarta.persistence.*;
import lombok.*;

/**
 * Association between threads and tags.
 */
@Entity
@Table(name = "thread_tags")
@IdClass(ThreadTagId.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ThreadTag {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "thread_id", nullable = false)
    private ForumThread thread;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id", nullable = false)
    private ForumTag tag;
}