package io.albot.distributedtransactions.service;

import io.albot.distributedtransactions.dao.JobRepository;
import io.albot.distributedtransactions.dao.SubJobRepository;
import io.albot.distributedtransactions.entity.Job;
import io.albot.distributedtransactions.entity.JobStatus;
import io.albot.distributedtransactions.entity.SubJob;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {
    private final JobRepository jobRepository;
    private final SubJobRepository subJobRepository;

    @Override
    public void changeSubJobStatus(SubJob subJob, JobStatus status) {
        subJob.setStatus(status);
        subJobRepository.save(subJob);
    }

    @Override
    public void changeJobStatus(Job job, JobStatus status) {
        job.setStatus(status);
        jobRepository.save(job);
    }
}
