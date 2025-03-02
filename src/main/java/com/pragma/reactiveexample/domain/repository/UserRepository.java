package com.pragma.reactiveexample.domain.repository;

import com.pragma.reactiveexample.domain.model.User;
import reactor.core.publisher.Mono;

public interface UserRepository {
    Mono<User> getUserById(String id);
    Mono<User> saveUser(User user);
}
