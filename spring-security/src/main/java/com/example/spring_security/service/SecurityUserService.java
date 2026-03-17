package com.example.spring_security.service;

import com.example.spring_security.entity.User;
import com.example.spring_security.enums.LoginIdentifierType;
import com.example.spring_security.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class SecurityUserService implements UserDetailsService {

    private final UserRepository repository;

    public SecurityUserService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String identifier)
            throws UsernameNotFoundException {

        LoginIdentifierType type = LoginIdentifierType.from(identifier);

        User user = switch (type) {

            case EMAIL ->  repository.findByEmail(identifier)
                    .orElseThrow(() ->
                            new UsernameNotFoundException("Email not found"));

            case PHONE ->  repository.findByPhno(identifier)
                    .orElseThrow(() ->
                            new UsernameNotFoundException("Phone not found"));

            case USERNAME ->  repository.findByUsername(identifier)
                    .orElseThrow(() ->
                            new UsernameNotFoundException("Username not found"));
        };


        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
//                .roles(user.getRole())
                .build();
    }
}