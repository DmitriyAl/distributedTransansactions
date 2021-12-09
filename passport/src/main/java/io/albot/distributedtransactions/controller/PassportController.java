package io.albot.distributedtransactions.controller;

import io.albot.distributedtransactions.dto.Passport;
import io.albot.distributedtransactions.service.PassportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequiredArgsConstructor
public class PassportController {
    private final PassportService passportService;
    private final AtomicInteger counter = new AtomicInteger();

    @PostMapping(value = "save")
    public Passport save(@RequestBody Passport passport) {
        passport = passportService.save(passport);
        if (counter.incrementAndGet() % 3 == 0) {
            throw new RuntimeException(String.format("An exception during passport saving with id %d has occurred", passport.getId()));
        }
        return passport;
    }

    @PostMapping(value = "clean")
    public void cleanUp(@RequestBody List<UUID> uuids) {
        System.out.println("cleanUp " + uuids);
        passportService.cleanUp(uuids);
    }
}
