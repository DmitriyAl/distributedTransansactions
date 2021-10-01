package io.albot.distributedtransactions.dao;

import io.albot.distributedtransactions.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JobRepository extends JpaRepository<Job, UUID> {
}
