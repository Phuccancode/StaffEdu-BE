package com.example.web_adventure.training_area_management.training_area_hierarchy.entity;

import java.io.Serializable;
import lombok.*;

/**
 * Composite primary key for TrainingAreaHierarchy.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TrainingAreaHierarchyId implements Serializable {
    private Long parentArea;
    private Long childArea;
}
