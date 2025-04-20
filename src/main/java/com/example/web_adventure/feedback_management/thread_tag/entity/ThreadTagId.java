package com.example.web_adventure.feedback_management.thread_tag.entity;

import java.io.Serializable;
import lombok.*;

/**
 * Composite key for ThreadTag.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ThreadTagId implements Serializable {
    private Long thread;
    private Long tag;
}
