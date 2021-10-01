package io.albot.distributedtransactions.controller;

import io.albot.distributedtransactions.dto.Passport;
import io.albot.distributedtransactions.service.PassportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequiredArgsConstructor
public class PassportController {
    private final PassportService passportService;

    @PostMapping(value = "save")
    public Passport save(@RequestBody Passport passport) {
        passport = passportService.save(passport);
        if (new Random().nextInt(10) > 5) {
            throw new RuntimeException();
        }
        return passport;
    }
}
