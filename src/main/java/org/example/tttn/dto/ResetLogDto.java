package org.example.tttn.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ResetLogDto {
    private Long id;
    private String username;
    private String email;
    private LocalDateTime resetAt;
    private String ipAddress;
    private String userAgent;
    private Boolean suspicious;
    private Double confidenceScore;
}