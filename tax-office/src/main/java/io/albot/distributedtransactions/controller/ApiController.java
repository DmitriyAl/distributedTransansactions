package io.albot.distributedtransactions.controller;

import io.albot.distributedtransactions.dto.TaxData;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
public class ApiController {
    @PostMapping(value = "save")
    public TaxData save(@RequestBody TaxData taxData) {
        System.out.println(taxData);
        if (new Random().nextBoolean()) {
            throw new RuntimeException();
        }
        return taxData;
    }
}
