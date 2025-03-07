package com.pragma.reactiveexample.application.port;

import com.pragma.reactiveexample.domain.model.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface UserServicePort {
    Mono<User> getUserById(String id);
    Mono<User> saveUser(User user);
    Flux<User> saveUsersBulk(Flux<User> users);  // Nuevo método para insertar en lotes
}
