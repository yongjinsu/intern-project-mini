package org.example.tttn.mapper;

import org.example.tttn.dto.ResetLogDto;
import org.example.tttn.entity.ResetLog;
import org.springframework.stereotype.Component;

@Component
public class ResetLogMapper {

    public ResetLogDto toDto(ResetLog entity) {
        if (entity == null) return null;
        
        ResetLogDto dto = new ResetLogDto();
        dto.setId(entity.getId());
        dto.setUsername(entity.getUsername());
        dto.setEmail(entity.getEmail());
        dto.setResetAt(entity.getResetAt());
        dto.setIpAddress(entity.getIpAddress());
        dto.setUserAgent(entity.getUserAgent());
        dto.setSuspicious(entity.getSuspicious());
        dto.setConfidenceScore(entity.getConfidenceScore());
        return dto;
    }

    public ResetLog toEntity(ResetLogDto dto) {
        if (dto == null) return null;
        
        ResetLog entity = new ResetLog();
        entity.setId(dto.getId());
        entity.setUsername(dto.getUsername());
        entity.setEmail(dto.getEmail());
        entity.setResetAt(dto.getResetAt());
        entity.setIpAddress(dto.getIpAddress());
        entity.setUserAgent(dto.getUserAgent());
        entity.setSuspicious(dto.getSuspicious());
        entity.setConfidenceScore(dto.getConfidenceScore());
        return entity;
    }
}