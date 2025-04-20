package com.example.web_adventure.roadmap_management.roadmap_training_area.entity;

import java.io.Serializable;
import lombok.*;

/**
 * Composite key for roadmap_training_areas.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RoadmapTrainingAreaId implements Serializable {
    private Long roadmap;
    private Long area;
}
