package io.albot.distributedtransactions.controller;

import io.albot.distributedtransactions.dto.TaxData;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
public class TaxOfficeController {
    @PostMapping(value = "save")
    public TaxData save(@RequestBody TaxData taxData) {
        System.out.println(taxData);
        if (new Random().nextInt(10) < 5) {
            throw new RuntimeException();
        }
        return taxData;
    }

    @PostMapping(value = "clean")
    public void cleanUp(@RequestBody List<UUID> uuids) throws InterruptedException {
        System.out.println("cleanUp " + uuids);
        TimeUnit.SECONDS.sleep(40);
    }
}
