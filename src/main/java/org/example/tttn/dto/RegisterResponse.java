package org.example.tttn.dto;

import lombok.Data;

@Data
public class RegisterResponse {
    private Long userId;
    private String username;
    private String email;
}
