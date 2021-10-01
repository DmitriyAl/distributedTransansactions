package io.albot.distributedtransactions.dao;

import io.albot.distributedtransactions.entity.PassportEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PassportRepository extends JpaRepository<PassportEntity, Long> {
    PassportEntity findByJobId(UUID jobId);
}
