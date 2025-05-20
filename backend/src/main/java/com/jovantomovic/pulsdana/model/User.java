package com.jovantomovic.pulsdana.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.Set;

@Data
//@Document(collation = "users")
public class User {
    @Id
    private String id;
    private String username;
    private String email;
    private String passwordHash;
    private Set<String> roles;
    private String refreshToken;
    @CreatedDate
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
}
