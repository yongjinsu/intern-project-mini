package org.example.tttn.service.interfaces;

/**
 * Interface cho Email Service
 * Xử lý việc gửi email cho các tính năng khác nhau
 */
public interface IEmailService {
    /**
     * Gửi email reset password với mật khẩu mới
     */
    boolean sendPasswordResetEmail(String toEmail, String username, String newPassword);
}
