package com.example.spring_security.controller;

import com.example.spring_security.dto.LoginRequest;
import com.example.spring_security.dto.RegisterRequest;
import com.example.spring_security.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    public AuthController(UserService userService,
                          AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    // ✅ Register new user
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {

        userService.registerUser(request);

        return ResponseEntity.ok("User registered successfully");
    }

    // ✅ Login manually (for REST-based login)
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.identifier(),
                        request.password()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return ResponseEntity.ok("Login successful");
    }

    // ✅ Get current logged-in user
    @GetMapping("/me")
    public ResponseEntity<String> currentUser(Authentication authentication) {

        return ResponseEntity.ok("Logged in as: " + authentication.getName());
    }

    // ✅ Logout (session-based)
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) throws Exception {

        request.logout(); // clears authentication & session

        return ResponseEntity.ok("Logged out successfully");
    }
}