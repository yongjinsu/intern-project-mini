package org.example.tttn.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordRequest {
    @NotNull
    private String oldPassword;
    @NotNull
    @Size(min = 8, max = 20)
    private String newPassword;
    @NotNull
    @Size(min = 8, max = 20)
    private String confirmPassword;
}
