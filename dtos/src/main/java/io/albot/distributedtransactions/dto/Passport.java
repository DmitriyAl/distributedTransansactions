package io.albot.distributedtransactions.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Passport {
    private Long id;
    private UUID jobId;
    private String firstName;
    private String lastName;
    private Integer series;
    private Integer number;

    public Passport(Integer series, Integer number) {
        this.series = series;
        this.number = number;
    }
}
