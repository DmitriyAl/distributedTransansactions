package io.albot.distributedtransactions.service;

import io.albot.distributedtransactions.dto.Passport;

import java.util.List;
import java.util.UUID;

public interface PassportService {
    Passport save(Passport passport);
    void cleanUp(List<UUID> uuids);
    Passport find(long id);

}
