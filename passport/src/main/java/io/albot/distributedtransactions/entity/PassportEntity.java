package io.albot.distributedtransactions.entity;

import io.albot.distributedtransactions.dto.Passport;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "passports")
@Data
@NoArgsConstructor
public class PassportEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private UUID jobId;
    private String firstName;
    private String lastName;
    private Integer series;
    private Integer number;

    public PassportEntity(Passport dto) {
        this.id = dto.getId();
        this.jobId = dto.getJobId();
        this.firstName = dto.getFirstName();
        this.lastName = dto.getLastName();
        this.series = dto.getSeries();
        this.number = dto.getNumber();
    }

    public Passport toDto() {
        return new Passport()
                .setId(this.getId())
                .setJobId(this.jobId)
                .setFirstName(this.getFirstName())
                .setLastName(this.getLastName())
                .setSeries(this.getSeries())
                .setNumber(this.getNumber());
    }
}
