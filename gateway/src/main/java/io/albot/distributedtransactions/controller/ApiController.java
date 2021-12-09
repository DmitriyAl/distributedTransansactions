package io.albot.distributedtransactions.controller;

import io.albot.distributedtransactions.dto.User;
import io.albot.distributedtransactions.sceduler.CleanUpScheduler;
import io.albot.distributedtransactions.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class ApiController {
    private final UserService userService;
    private final CleanUpScheduler cleanUpScheduler;

    @PostMapping(value = "save")
    public Mono<User> save(@RequestBody User user) {
        return userService.save(user);
    }

    @DeleteMapping(value = "clean")
    public void clean() {
        cleanUpScheduler.cleanUpUsers();
    }

    public Mono<User> find(long id) {
        return userService.find(id);
    }
}
