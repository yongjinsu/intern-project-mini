package org.example.tttn.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ForgetPasswordRequest {
    @NotNull
    private String email;
}
