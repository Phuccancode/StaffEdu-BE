package com.example.web_adventure.roadmap_management.roadmap_statistic.entity;

import com.example.web_adventure.roadmap_management.user_roadmap.entity.UserRoadmap;
import com.example.web_adventure.user_management.employee.entity.Employee;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Aggregated roadmap metrics.
 */
@Entity
@Table(name = "roadmap_statistics")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoadmapStatistic {

    @Id
    @Column(name = "stat_id", nullable = false, updatable = false)
    private Long statId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roadmap_id", nullable = false)
    private UserRoadmap roadmap;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "areas_total", nullable = false)
    private Integer areasTotal;

    @Column(name = "areas_completed", nullable = false)
    private Integer areasCompleted;

    @Column(name = "courses_total", nullable = false)
    private Integer coursesTotal;

    @Column(name = "courses_completed", nullable = false)
    private Integer coursesCompleted;

    @Column(name = "average_progress", nullable = false)
    private Short averageProgress;

    @Column(name = "average_score")
    private java.math.BigDecimal averageScore;

    @Column(name = "pass_rate", precision = 5, scale = 2)
    private java.math.BigDecimal passRate;

    @Column(name = "total_time_spent", nullable = false)
    private Integer totalTimeSpent;

    @Column(name = "courses_dropped", nullable = false)
    private Integer coursesDropped;

    @CreationTimestamp
    @Column(name = "last_calculated_at", nullable = false, updatable = false)
    private LocalDateTime lastCalculatedAt;
}