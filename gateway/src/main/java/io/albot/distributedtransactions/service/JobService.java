package io.albot.distributedtransactions.service;

import io.albot.distributedtransactions.entity.Job;
import io.albot.distributedtransactions.entity.JobStatus;
import io.albot.distributedtransactions.entity.SubJob;

public interface JobService {
    void changeSubJobStatus(SubJob subJob, JobStatus status);

    void changeJobStatus(Job job, JobStatus status);
}
