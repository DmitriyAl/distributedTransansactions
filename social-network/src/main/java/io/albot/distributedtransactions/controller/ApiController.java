package io.albot.distributedtransactions.controller;

import io.albot.distributedtransactions.dto.SocialNetworkData;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {
    @PostMapping(value = "save")
    public SocialNetworkData save(@RequestBody SocialNetworkData socialNetworkData){
        System.out.println(socialNetworkData);
        return socialNetworkData;
    }
}
