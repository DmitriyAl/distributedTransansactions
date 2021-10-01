package io.albot.distributedtransactions.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "sub_jobs")
@Data
@NoArgsConstructor
public class SubJob {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;
    @ManyToOne(targetEntity = Job.class)
    @JoinColumn(name = "job_id")
    private Job job;
    @Enumerated(EnumType.STRING)
    private JobStatus status;

    public SubJob(JobStatus status) {
        this.status = status;
    }
}
