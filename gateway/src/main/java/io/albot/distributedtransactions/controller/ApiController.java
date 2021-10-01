package io.albot.distributedtransactions.controller;

import io.albot.distributedtransactions.dto.User;
import io.albot.distributedtransactions.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class ApiController {
    private final UserService userService;

    public ApiController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "save")
    public Mono<User> save(@RequestBody User user) {
        return userService.save(user);
    }

    public Mono<User> find(long id) {
        return userService.find(id);
    }
}
