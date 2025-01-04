package org.yug.springsecdemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import org.yug.springsecdemo.model.User;
import org.yug.springsecdemo.service.JwtService;
import org.yug.springsecdemo.service.UserService;

@RestController

public class UserController {

    @Autowired
    private UserService service;

    @Autowired
    private JwtService jwtService;

    @Autowired
    AuthenticationManager authenticationManager;

    // Register endpoint
    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return service.saveUser(user);
    }

    // Login endpoint
    @PostMapping("/login")
    public String login(@RequestBody User user) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(user.getUsername());
        } else {
            throw new RuntimeException("Login failed: Invalid credentials");
        }
    }

    // Endpoint to get the current user's details
    @GetMapping("/user")
    public String getUserDetails(@RequestParam String token) {
        String username = jwtService.extractUsername(token);
        return "Welcome, " + username + "!";
    }
}
