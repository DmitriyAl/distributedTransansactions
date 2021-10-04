package io.albot.distributedtransactions.service;

import io.albot.distributedtransactions.dto.User;
import io.albot.distributedtransactions.entity.Job;
import reactor.core.publisher.Mono;

import java.util.List;

public interface UserService {
    Mono<User> save(User user);
    Mono<User> find(long id);
    void deleteByJobs(List<Job> jobs);
}
