package com.pragma.reactiveexample.application.port;

import com.pragma.reactiveexample.domain.model.User;
import reactor.core.publisher.Mono;

public interface UserRepositoryPort {
    Mono<User> getUserById(String id);
    Mono<User> saveUser(User user);
}