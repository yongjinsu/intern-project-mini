package org.example.tttn.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {
    private String secretKey = "mySecretKeyForJWTTokenGenerationThatIsLongEnoughForHS256Algorithm";
    private Long accessExpiration = 86400000L; // 24 hours
    private Long refreshExpiration = 604800000L; // 7 days
}