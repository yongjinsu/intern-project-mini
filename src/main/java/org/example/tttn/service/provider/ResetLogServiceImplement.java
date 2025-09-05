package org.example.tttn.service.provider;

import lombok.RequiredArgsConstructor;
import org.example.tttn.dto.PredictionRequest;
import org.example.tttn.dto.ResetLogDto;
import org.example.tttn.entity.ResetLog;
import org.example.tttn.mapper.ResetLogMapper;
import org.example.tttn.repository.ResetLogRepository;
import org.example.tttn.service.interfaces.IEmailService;
import org.example.tttn.service.interfaces.IResetLogService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ResetLogServiceImplement implements IResetLogService {

    private final ResetLogRepository resetLogRepository;
    private final IEmailService emailService;
    private final RestTemplate restTemplate;
    private final ResetLogMapper resetLogMapper;

    @Value("${app.prediction.api.url:http://localhost:5000/predict}")
    private String predictionApiUrl;

    @Override
    public ResetLogDto logPasswordReset(String username, String email, String ipAddress, String userAgent) {
        ResetLog resetLog = new ResetLog();
        resetLog.setUsername(username);
        resetLog.setEmail(email);
        resetLog.setResetAt(LocalDateTime.now());
        resetLog.setIpAddress(ipAddress);
        resetLog.setUserAgent(userAgent);
        resetLog.setSuspicious(false);
        resetLog.setConfidenceScore(0.0);
        
        ResetLog savedLog = resetLogRepository.save(resetLog);
        return resetLogMapper.toDto(savedLog);
    }

    @Override
    public void analyzeSuspiciousActivity(ResetLogDto resetLogDto) {
        try {
            ResetLog resetLog = resetLogMapper.toEntity(resetLogDto);
            
            ResponseEntity<PredictionRequest> response = restTemplate.postForEntity(
                predictionApiUrl, 
                resetLog, 
                PredictionRequest.class
            );

            if (response.getBody() != null) {
                PredictionRequest result = response.getBody();
                
                resetLog.setSuspicious(result.isSuspicious());
                resetLog.setConfidenceScore(result.getConfidence_score());
                resetLogRepository.save(resetLog);

                if (result.isSuspicious()) {
                    emailService.sendSecurityAlert(resetLog.getEmail(), resetLog.getUsername(), resetLog);
                }
            }
        } catch (Exception e) {
            // Log error but don't fail the reset process
        }
    }
}