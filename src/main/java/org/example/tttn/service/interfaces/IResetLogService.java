package org.example.tttn.service.interfaces;

import org.example.tttn.entity.ResetLog;

import org.example.tttn.dto.ResetLogDto;

public interface IResetLogService {
    ResetLogDto logPasswordReset(String username, String email, String ipAddress, String userAgent);
    void analyzeSuspiciousActivity(ResetLogDto resetLogDto);
}