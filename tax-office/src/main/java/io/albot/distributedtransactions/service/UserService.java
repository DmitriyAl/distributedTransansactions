package io.albot.distributedtransactions.service;

import io.albot.distributedtransactions.dto.User;

public interface UserService {
    User save(User user);
    User find(long id);
}
