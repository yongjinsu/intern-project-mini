package org.example.tttn.service.interfaces;

import org.example.tttn.dto.*;

public interface IAuthService {
    RegisterResponse register(RegisterRequest request);
    LoginResponse login(LoginRequest request);
    void logout(String token);
    LoginResponse refreshToken(String refreshToken);
    ForgetPasswordResponse resetPassword(ForgetPasswordRequest request);
    ChangePasswordResponse changePassword(Long userId, ChangePasswordRequest request);
    void logoutAllDevices(Long userId);
}
