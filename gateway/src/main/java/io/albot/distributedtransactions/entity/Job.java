package io.albot.distributedtransactions.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "jobs")
@Data
@NoArgsConstructor
public class Job {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;
    @Enumerated(EnumType.STRING)
    private JobStatus status;
    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL)
    private List<SubJob> subJobs;

    public Job(JobStatus status) {
        this.status = status;
    }
}
