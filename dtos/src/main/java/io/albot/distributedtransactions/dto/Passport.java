package io.albot.distributedtransactions.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Passport {
    private Long id;
    private String firstName;
    private String lastName;
    private Integer series;
    private Integer number;

    public Passport(Integer series, Integer number) {
        this.series = series;
        this.number = number;
    }
}
