package com.example.web_adventure.progress_management.certificate.entity;

import com.example.web_adventure.progress_management.enrollment.entity.Enrollment;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Certificate issued upon course completion.
 */
@Entity
@Table(name = "certificates")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Certificate {

    @Id
    @Column(name = "certificate_id", nullable = false, updatable = false)
    private Long certificateId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollment_id", nullable = false)
    private Enrollment enrollment;

    @Column(name = "issued_date", nullable = false)
    private LocalDate issuedDate;

    @Column(name = "certificate_url", length = 500)
    private String certificateUrl;

    @Column(columnDefinition = "TEXT")
    private String remarks;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
