package com.pragma.reactiveexample.application.handler;


import com.pragma.reactiveexample.application.port.UserServicePort;
import com.pragma.reactiveexample.application.service.UserServiceImpl;
import com.pragma.reactiveexample.domain.model.User;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserHandler {

    private final UserServicePort userServicePort;

    public UserHandler(UserServiceImpl userServiceImpl) {
        this.userServicePort = userServiceImpl;
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<User> getUserById(@PathVariable String id) {
        return userServicePort.getUserById(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<User> saveUser(@RequestBody User user) {
        return userServicePort.saveUser(user);
    }

    @PostMapping(value = "/bulk", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<User> saveUsersBulk(@RequestBody Flux<User> users) {
        return userServicePort.saveUsersBulk(users);
    }
    @GetMapping(value = "/testing", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<User> testing() {
        List<User> users = new ArrayList<>();
        users.add(new User("1", "John", "Doe"));
        users.add(new User("1", "John", "Doe"));
        users.add(new User("1", "John", "Doe"));
        users.add(new User("1", "John", "Doe"));
        return Flux.fromIterable(users).delayElements(Duration.ofSeconds(10));
    }

}
