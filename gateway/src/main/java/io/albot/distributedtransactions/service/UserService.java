package io.albot.distributedtransactions.service;

import io.albot.distributedtransactions.dto.User;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<User> save(User user);
    Mono<User> find(long id);
}
