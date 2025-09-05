package org.example.tttn.repository;

import org.example.tttn.entity.ResetLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResetLogRepository extends JpaRepository<ResetLog, Long> {
}