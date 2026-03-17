package com.example.spring_security.service;

import com.example.spring_security.dto.RegisterRequest;
import com.example.spring_security.entity.User;
import com.example.spring_security.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository repo ;
    private final PasswordEncoder encoder;

    public UserService(UserRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    public void registerUser(RegisterRequest request) {
        String hashedPassword = encoder.encode(request.password());

        // send for otp

        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .phno(request.phno())
                .password(hashedPassword)
                .build();
        repo.save(user);
    }
}
