package io.albot.distributedtransactions.controller;

import io.albot.distributedtransactions.dto.TaxData;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
public class ApiController {
    @PostMapping(value = "save")
    public TaxData save(@RequestBody TaxData taxData) throws InterruptedException {
        System.out.println(taxData);
        TimeUnit.SECONDS.sleep(10);
        return taxData;
    }
}
