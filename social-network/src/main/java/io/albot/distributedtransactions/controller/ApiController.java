package io.albot.distributedtransactions.controller;

import io.albot.distributedtransactions.dto.SocialNetworkData;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
public class ApiController {
    @PostMapping(value = "save")
    public SocialNetworkData save(@RequestBody SocialNetworkData socialNetworkData) throws InterruptedException {
        System.out.println(socialNetworkData);
        TimeUnit.SECONDS.sleep(15);
        return socialNetworkData;
    }
}
