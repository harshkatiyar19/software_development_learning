package com.example.spring_security.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Entity
@Getter
@Setter
@Table(name = "users")

public class User {

    @Id
    @GeneratedValue
    private Long id;

    private String username;

    private String email;
    private String phno;
    private String password;
//    private String role;
}