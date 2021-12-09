package io.albot.distributedtransactions.controller;

import io.albot.distributedtransactions.dto.SocialNetworkData;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class SocialNetworkController {
    @PostMapping(value = "save")
    public SocialNetworkData save(@RequestBody SocialNetworkData socialNetworkData) {
        System.out.println(socialNetworkData);
        if (socialNetworkData.getId() % 4 == 0) {
            throw new RuntimeException(String.format("An exception during socialNetworkData saving with id %d has occurred", socialNetworkData.getId()));
        }
        return socialNetworkData;
    }

    @PostMapping(value = "clean")
    public void cleanUp(@RequestBody List<UUID> uuids) {
        System.out.println("cleanUp " + uuids);
    }
}
