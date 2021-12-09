package io.albot.distributedtransactions.controller;

import io.albot.distributedtransactions.dto.Passport;
import io.albot.distributedtransactions.dto.SocialNetworkData;
import io.albot.distributedtransactions.dto.TaxData;
import io.albot.distributedtransactions.dto.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

@SpringBootTest
class ApiControllerTest {

    @Test
    public void create20Users() {
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            SocialNetworkData snd = new SocialNetworkData().setId((long) i).setVkId(random.nextLong());
            TaxData td = new TaxData().setId((long) i).setTaxNumber(random.nextLong());
            Passport p = new Passport().setNumber(random.nextInt(1000000)).setSeries(random.nextInt(10000)).setFirstName("Name" + i).setLastName("LastNAme" + i);
            User user = new User(null, p, td, snd);
            try {
                System.out.println(new RestTemplate().postForObject("http://localhost:8081/save", user, User.class));
            } catch (Exception exception) {
            }
        }
    }
}