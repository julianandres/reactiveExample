package com.pragma.reactiveexample.domain.model;


import lombok.*;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@DynamoDbBean
public class User {
    private String id;
    private String name;
    private String email;

    @DynamoDbPartitionKey
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }

}
