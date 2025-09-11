package org.example.tttn.controller;

import lombok.RequiredArgsConstructor;
import org.example.tttn.config.ApiResponse;
import org.example.tttn.constants.MessageCode;
import org.example.tttn.dto.ChangePasswordRequest;
import org.example.tttn.dto.ChangePasswordResponse;
import org.example.tttn.dto.PasswordSuggestionResponse;
import org.example.tttn.service.interfaces.IAIService;
import org.example.tttn.service.interfaces.IAuthService;
import org.example.tttn.util.SecurityUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final IAuthService authService;
    private final IAIService aiService;

    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<ChangePasswordResponse>> changePassword(
            @RequestBody ChangePasswordRequest request) {
        try {
            Long userId = SecurityUtil.getCurrentUserId();
            ChangePasswordResponse response = authService.changePassword(userId, request);
            return ResponseEntity.ok(ApiResponse.success(response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.error(HttpStatus.BAD_REQUEST.value(), MessageCode.AUTH_INVALID_TOKEN)
            );
        }
    }

    @GetMapping("/password-suggestions")
    public ResponseEntity<ApiResponse<PasswordSuggestionResponse>> getPasswordSuggestions() {
        try {
            Long userId = SecurityUtil.getCurrentUserId();
            String suggestions = aiService.generatePasswordSuggestions(userId.toString());
            PasswordSuggestionResponse response = new PasswordSuggestionResponse(suggestions);
            return ResponseEntity.ok(ApiResponse.success(response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.error(HttpStatus.BAD_REQUEST.value(), MessageCode.AUTH_INVALID_TOKEN)
            );
        }
    }
}