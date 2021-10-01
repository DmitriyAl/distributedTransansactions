package io.albot.distributedtransactions.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "jobs")
@Data
public class Job {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;
    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL)
    private List<SubJob> subJobs;
}
