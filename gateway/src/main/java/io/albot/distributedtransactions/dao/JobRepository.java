package io.albot.distributedtransactions.dao;

import io.albot.distributedtransactions.entity.Job;
import io.albot.distributedtransactions.entity.JobStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JobRepository extends JpaRepository<Job, UUID> {
    List<Job> findAllByStatus(JobStatus status);
}
