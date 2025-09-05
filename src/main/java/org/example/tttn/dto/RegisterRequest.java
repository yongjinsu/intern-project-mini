package org.example.tttn.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @NotNull
    private String username;

    @NotNull
    private String password;

    @NotNull
    private String email;
    private String phone;
    private String address;
}
