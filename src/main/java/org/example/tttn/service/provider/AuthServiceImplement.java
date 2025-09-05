package org.example.tttn.service.provider;

import lombok.RequiredArgsConstructor;
import org.example.tttn.dto.*;
import org.example.tttn.entity.User;
import org.example.tttn.entity.UserToken;
import org.example.tttn.mapper.UserMapper;
import org.example.tttn.repository.UserRepository;
import org.example.tttn.repository.UserTokenRepository;
import org.example.tttn.security.JwtUtil;
import org.example.tttn.service.interfaces.IAuthService;
import org.example.tttn.service.interfaces.IEmailService;
import org.example.tttn.service.interfaces.IRedisService;
import org.example.tttn.service.interfaces.IResetLogService;
import org.example.tttn.util.PasswordGenerator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImplement implements IAuthService {

    private final UserRepository userRepository;
    private final UserTokenRepository userTokenRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final IRedisService redisService;
    private final IEmailService emailService;
    private final IResetLogService resetLogService;
    private final PasswordGenerator passwordGenerator;

    /**
     * Đăng kí tài khoản
     */
    @Override
    public RegisterResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        User savedUser = userRepository.save(user);

        return userMapper.toRegisterResponse(savedUser);
    }

    /**
     * Đăng nhập tài khoản
     */
    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Incorrect password");
        }

        String accessToken = jwtUtil.generateAccessToken(user.getId(), "NORMAL", "BRONZE");
        String refreshToken = jwtUtil.generateRefreshToken(user.getId());

        // Lưu token vào database để quản lý logout all devices
        saveUserToken(user.getId(), jwtUtil.getJtiFromToken(accessToken), jwtUtil.getExpirationFromToken(accessToken));
        saveUserToken(user.getId(), jwtUtil.getJtiFromToken(refreshToken), jwtUtil.getExpirationFromToken(refreshToken));

        LoginResponse response = userMapper.toLoginResponse(user);
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);
        return response;
    }

    /**
     * Đăng xuất tài khoản
     */
    @Override
    public void logout(String token) {
        try {
            String jti = jwtUtil.getJtiFromToken(token);
            long expiration = jwtUtil.getExpirationFromToken(token).getTime();
            
            // Thêm vào blacklist
            redisService.addTokenToBlacklist(jti, expiration);
            
            // Xóa khỏi database
            userTokenRepository.deleteByJti(jti);

        } catch (Exception e) {
            throw new RuntimeException("Invalid token");
        }
    }

    /**
     * Refresh token
     */
    @Override
    public LoginResponse refreshToken(String refreshToken) {
        try {
            if (!jwtUtil.validateToken(refreshToken)) {
                throw new RuntimeException("Invalid or expired refresh token");
            }
            Long userId = jwtUtil.getUserIdFromToken(refreshToken);
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            String newAccessToken = jwtUtil.generateAccessToken(user.getId(), "NORMAL", "BRONZE");
            String newRefreshToken = jwtUtil.generateRefreshToken(user.getId());

            // Lưu token mới
            saveUserToken(user.getId(), jwtUtil.getJtiFromToken(newAccessToken), jwtUtil.getExpirationFromToken(newAccessToken));
            saveUserToken(user.getId(), jwtUtil.getJtiFromToken(newRefreshToken), jwtUtil.getExpirationFromToken(newRefreshToken));

            LoginResponse response = userMapper.toLoginResponse(user);
            response.setAccessToken(newAccessToken);
            response.setRefreshToken(newRefreshToken);
            return response;

        } catch (Exception e) {
            throw new RuntimeException("Invalid refresh token");
        }
    }

    /**
     * Reset mật khẩu
     */
    @Override
    public ForgetPasswordResponse resetPassword(ForgetPasswordRequest request) {
        try {
            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found with email: " + request.getEmail()));

            String newPassword = passwordGenerator.generateSecurePassword();
            String hashedPassword = passwordEncoder.encode(newPassword);
            user.setPassword(hashedPassword);
            userRepository.save(user);

            // Ghi log reset password
            String ipAddress = getClientIpAddress();
            String userAgent = getUserAgent();
            ResetLogDto resetLogDto = resetLogService.logPasswordReset(user.getUsername(), request.getEmail(), ipAddress, userAgent);
            
            // Phân tích hoạt động đáng ngờ
            resetLogService.analyzeSuspiciousActivity(resetLogDto);

            logoutAllDevices(user.getId());

            boolean emailSent = emailService.sendPasswordResetEmail(
                    request.getEmail(),
                    user.getUsername(),
                    newPassword
            );

            if (!emailSent) {
                throw new RuntimeException("Failed to send reset password email");
            }

            return new ForgetPasswordResponse("Password reset successfully. Please check your email for the new password.", request.getEmail());

        } catch (Exception e) {
            throw new RuntimeException("Password reset failed: " + e.getMessage());
        }
    }

    @Override
    public ChangePasswordResponse changePassword(Long userId, ChangePasswordRequest request) {
        try {
            if (!request.getNewPassword().equals(request.getConfirmPassword())) {
                throw new RuntimeException("New password and confirm password do not match");
            }

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
                throw new RuntimeException("Old password is incorrect");
            }

            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(user);

            // Logout tất cả devices sau khi đổi mật khẩu
            logoutAllDevices(userId);

            return new ChangePasswordResponse("Password changed successfully");

        } catch (Exception e) {
            throw new RuntimeException("Change password failed: " + e.getMessage());
        }
    }

    @Override
    public void logoutAllDevices(Long userId) {
        try {
            // Lấy tất cả token còn hiệu lực của user
            List<UserToken> activeTokens = userTokenRepository.findByUserIdAndExpiresAtAfter(userId, LocalDateTime.now());
            
            // Thêm tất cả vào blacklist
            for (UserToken token : activeTokens) {
                long expiration = token.getExpiresAt().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                redisService.addTokenToBlacklist(token.getJti(), expiration);
            }
            
            // Xóa tất cả token của user khỏi database
            userTokenRepository.deleteByUserId(userId);
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to logout all devices: " + e.getMessage());
        }
    }

    private void saveUserToken(Long userId, String jti, java.util.Date expiration) {
        UserToken userToken = new UserToken();
        userToken.setUserId(userId);
        userToken.setJti(jti);
        userToken.setExpiresAt(expiration.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        userTokenRepository.save(userToken);
    }

    private String getClientIpAddress() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                String xForwardedFor = request.getHeader("X-Forwarded-For");
                if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
                    return xForwardedFor.split(",")[0].trim();
                }
                return request.getRemoteAddr();
            }
        } catch (Exception e) {
            // Log error if needed
        }
        return "unknown";
    }

    private String getUserAgent() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                return request.getHeader("User-Agent");
            }
        } catch (Exception e) {
            // Log error if needed
        }
        return "unknown";
    }
}
