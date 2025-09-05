package org.example.tttn.service.interfaces;

public interface IRedisService {
    void addTokenToBlacklist(String token, long expirationMillis);
    boolean isTokenBlacklisted(String token);
}
