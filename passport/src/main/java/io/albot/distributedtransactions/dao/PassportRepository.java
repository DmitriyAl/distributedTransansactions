package io.albot.distributedtransactions.dao;

import io.albot.distributedtransactions.entity.PassportEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PassportRepository extends JpaRepository<PassportEntity, Long> {
    PassportEntity findByJobId(UUID jobId);
    void deleteAllByJobIdIn(List<UUID> uuids);
}
