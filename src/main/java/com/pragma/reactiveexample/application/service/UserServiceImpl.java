package com.pragma.reactiveexample.application.service;

import com.pragma.reactiveexample.application.port.UserRepositoryPort;
import com.pragma.reactiveexample.application.port.UserServicePort;
import com.pragma.reactiveexample.domain.model.User;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserServicePort {

    private final UserRepositoryPort userRepository;


    private final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    public UserServiceImpl(UserRepositoryPort userRepository) {
        this.userRepository = userRepository;
    }

    @CircuitBreaker(name = "userService", fallbackMethod = "fallbackGetUserById")
    @Retry(name = "userService")
    @Override
    public Mono<User> getUserById(String id) {
        return userRepository.getUserById(id);
    }

    @CircuitBreaker(name = "userService", fallbackMethod = "fallbackSaveUser")
    @Retry(name = "userService")
    @Override
    public Mono<User> saveUser(User user) {
        return userRepository.saveUser(user);
    }

    @Override
    public Flux<User> saveUsersBulk(List<User> users) {
        return Flux.fromIterable(users)
                .map(user -> {
                    user.setId(UUID.randomUUID().toString());
                    return user;
                })
                .flatMap(userRepository::saveUser)
                .doOnNext(user -> log.info("✅ Usuario guardado: {}" ,user.getName()))
                .onErrorResume(e -> Mono.empty()); //
    }

    private Mono<User> fallbackGetUserById(String id, Throwable ex) {
        return Mono.just(new User());
    }

    private Mono<User> fallbackSaveUser(User user, Throwable ex) {
        return Mono.error(new RuntimeException("No se pudo guardar el usuario en este momento"));
    }
}
