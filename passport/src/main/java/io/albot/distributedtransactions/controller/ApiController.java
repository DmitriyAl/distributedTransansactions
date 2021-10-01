package io.albot.distributedtransactions.controller;

import io.albot.distributedtransactions.dto.Passport;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {
    @PostMapping(value = "save")
    public Passport save(@RequestBody Passport passport) {
        System.out.println(passport);
        return passport;
    }
}
