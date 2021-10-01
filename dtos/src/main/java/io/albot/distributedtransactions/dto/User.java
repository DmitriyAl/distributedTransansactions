package io.albot.distributedtransactions.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class User {
    private Long id;
    private Passport passport;
    private TaxData taxData;
    private SocialNetworkData socialNetworkData;

    public User(Passport passport) {
        this.passport = passport;
    }
}
