package io.albot.distributedtransactions.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Passport passport;
    private TaxData taxData;
    private SocialNetworkData socialNetworkData;

    public User(Passport passport) {
        this.passport = passport;
    }
}
