package org.example.tttn.dto;

import lombok.Data;

@Data
public class LoginResponse {
    private Long userId;
    private String accessToken;
    private String refreshToken;
    private String username;
    private String fullName;
}
