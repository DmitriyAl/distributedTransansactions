package io.albot.distributedtransactions.controller;

import io.albot.distributedtransactions.dto.TaxData;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class TaxOfficeController {

    @PostMapping(value = "save")
    public TaxData save(@RequestBody TaxData taxData) {
        System.out.println(taxData);
        if (taxData.getId() % 5 == 0) {
            throw new RuntimeException(String.format("An exception during socialNetworkData saving with id %d has occurred", taxData.getId()));
        }
        return taxData;
    }

    @PostMapping(value = "clean")
    public void cleanUp(@RequestBody List<UUID> uuids) {
        System.out.println("cleanUp " + uuids);
    }
}
