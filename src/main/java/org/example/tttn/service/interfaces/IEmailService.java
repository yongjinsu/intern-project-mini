package org.example.tttn.service.interfaces;

import org.example.tttn.entity.ResetLog;

/**
 * Interface cho Email Service
 * Xử lý việc gửi email cho các tính năng khác nhau
 */
public interface IEmailService {
    /**
     * Gửi email reset password với mật khẩu mới
     */
    boolean sendPasswordResetEmail(String toEmail, String username, String newPassword);
    
    /**
     * Gửi cảnh báo bảo mật
     */
    boolean sendSecurityAlert(String toEmail, String username, ResetLog resetLog);
}
