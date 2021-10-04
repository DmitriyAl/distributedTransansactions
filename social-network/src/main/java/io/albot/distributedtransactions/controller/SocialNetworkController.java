package io.albot.distributedtransactions.controller;

import io.albot.distributedtransactions.dto.SocialNetworkData;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
public class SocialNetworkController {
    @PostMapping(value = "save")
    public SocialNetworkData save(@RequestBody SocialNetworkData socialNetworkData) {
        System.out.println(socialNetworkData);
        if (new Random().nextInt(10) > 5) {
            throw new RuntimeException();
        }
        return socialNetworkData;
    }

    @PostMapping(value = "clean")
    public void cleanUp(@RequestBody List<UUID> uuids) throws InterruptedException {
        System.out.println("cleanUp " + uuids);
        TimeUnit.SECONDS.sleep(30);
    }
}
