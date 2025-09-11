package org.example.tttn.controller;

import lombok.RequiredArgsConstructor;
import org.example.tttn.config.ApiResponse;
import org.example.tttn.constants.MessageCode;
import org.example.tttn.dto.*;
import org.example.tttn.security.JwtUtil;
import org.example.tttn.service.interfaces.IAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IAuthService authService;
//    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponse>> register(@RequestBody RegisterRequest request) {
        RegisterResponse response = authService.register(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success(MessageCode.AUTH_LOGIN_SUCCESS, response));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(@RequestHeader("Authorization") String token) {
        String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
        authService.logout(jwtToken);
        return ResponseEntity.ok(ApiResponse.success(MessageCode.AUTH_LOGOUT_SUCCESS, "Logged out successfully"));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<LoginResponse>> refreshToken(@RequestBody String refreshToken) {
        LoginResponse response = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(ApiResponse.success(MessageCode.AUTH_TOKEN_REFRESHED, response));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<ForgetPasswordResponse>> forgotPassword(@RequestBody ForgetPasswordRequest request) {
        ForgetPasswordResponse response = authService.resetPassword(request);
        return ResponseEntity.ok(ApiResponse.success(MessageCode.AUTH_PASSWORD_RESET_SUCCESS, response));
    }


}