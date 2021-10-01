package io.albot.distributedtransactions.controller;

import io.albot.distributedtransactions.dto.Passport;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
public class ApiController {
    @PostMapping(value = "save")
    public Passport save(@RequestBody Passport passport) throws InterruptedException {
        System.out.println(passport);
        TimeUnit.SECONDS.sleep(30);
        return passport;
    }
}
