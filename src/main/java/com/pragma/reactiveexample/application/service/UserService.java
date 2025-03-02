package com.pragma.reactiveexample.application.service;

import com.pragma.reactiveexample.domain.model.User;
import com.pragma.reactiveexample.domain.repository.UserRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @CircuitBreaker(name = "userService", fallbackMethod = "fallbackGetUserById")
    @Retry(name = "userService")
    public Mono<User> getUserById(String id) {
        return userRepository.getUserById(id);
    }

    @CircuitBreaker(name = "userService", fallbackMethod = "fallbackSaveUser")
    @Retry(name = "userService")
    public Mono<User> saveUser(User user) {
        return userRepository.saveUser(user);
    }

    private Mono<User> fallbackGetUserById(String id, Throwable ex) {
        return Mono.just(new User());
    }

    private Mono<User> fallbackSaveUser(User user, Throwable ex) {
        return Mono.error(new RuntimeException("No se pudo guardar el usuario en este momento"));
    }
}
