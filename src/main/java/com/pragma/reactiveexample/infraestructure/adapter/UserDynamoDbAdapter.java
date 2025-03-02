package com.pragma.reactiveexample.infraestructure.adapter;

import com.pragma.reactiveexample.domain.model.User;
import com.pragma.reactiveexample.domain.repository.UserRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;

@Component
public class UserDynamoDbAdapter implements UserRepository {

    private final DynamoDbTable<User> userTable;

    public UserDynamoDbAdapter(DynamoDbClient dynamoDbClient) {
        DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();
        this.userTable = enhancedClient.table("Users", TableSchema.fromBean(User.class));
    }

    @Override
    public Mono<User> getUserById(String id) {
        return Mono.fromCallable(() -> userTable.getItem(r -> r.key(k -> k.partitionValue(id))))
                .onErrorResume(ResourceNotFoundException.class, e -> Mono.empty());
    }

    @Override
    public Mono<User> saveUser(User user) {
        return Mono.fromRunnable(() -> userTable.putItem(user))
                .thenReturn(user);
    }
}
