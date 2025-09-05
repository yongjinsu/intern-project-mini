package org.example.tttn.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "reset_logs")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResetLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "reset_at")
    private LocalDateTime resetAt;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "user_agent")
    private String userAgent;

    @Column(name = "suspicious")
    private Boolean suspicious;

    @Column(name = "confidence_score")
    private Double confidenceScore;
}
