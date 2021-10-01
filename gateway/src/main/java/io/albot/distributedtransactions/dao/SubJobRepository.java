package io.albot.distributedtransactions.dao;

import io.albot.distributedtransactions.entity.SubJob;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SubJobRepository extends JpaRepository<SubJob, UUID> {
}
