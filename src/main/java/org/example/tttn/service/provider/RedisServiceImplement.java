package org.example.tttn.service.provider;

import lombok.RequiredArgsConstructor;
import org.example.tttn.service.JwtBlacklistService;
import org.example.tttn.service.interfaces.IRedisService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisServiceImplement implements IRedisService {

    private final JwtBlacklistService jwtBlacklistService;

    @Override
    public void addTokenToBlacklist(String token, long expirationMillis) {
        jwtBlacklistService.addToBlacklist(token, expirationMillis);
    }

    @Override
    public boolean isTokenBlacklisted(String token) {
        return jwtBlacklistService.isBlacklisted(token);
    }
}
