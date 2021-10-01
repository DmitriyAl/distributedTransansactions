package io.albot.distributedtransactions.service;

import io.albot.distributedtransactions.dto.Passport;

public interface PassportService {
    Passport save(Passport passport);
    Passport find(long id);

}
