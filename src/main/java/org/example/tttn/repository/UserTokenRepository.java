package org.example.tttn.repository;

import org.example.tttn.entity.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserTokenRepository extends JpaRepository<UserToken, Long> {
    List<UserToken> findByUserIdAndExpiresAtAfter(Long userId, LocalDateTime now);
    void deleteByUserId(Long userId);
    void deleteByJti(String jti);
}