package com.example.demo.application.services;

import com.example.demo.application.Response.ApiResponse;
import com.example.demo.application.dto.LoginRequest;
import com.example.demo.application.dto.RegisterRequest;
import com.example.demo.domain.models.Role;
import com.example.demo.domain.models.User;
import com.example.demo.domain.repositories.RoleRepository;
import com.example.demo.domain.repositories.UserRepository;
import com.example.demo.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public ApiResponse register(RegisterRequest request) {
        logger.info("Attempting to register user: {}", request.getUsername());

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            logger.warn("Username already exists: {}", request.getUsername());
            return ApiResponse.error("Username already exists");
        }
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            logger.warn("Email already exists: {}", request.getEmail());
            return ApiResponse.error("Email already exists");
        }

        Role role = roleRepository.findByName("USER")
                .orElseGet(() -> {
                    logger.info("Default role USER not found, creating...");
                    return roleRepository.save(new Role("USER"));
                });

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setRoles(Set.of(role));
        userRepository.save(user);

        logger.info("User registered successfully: {}", user.getUsername());
        return ApiResponse.ok("User registered");
    }

    public ApiResponse login(LoginRequest request) {
        logger.info("Attempting login for email: {}", request.email());

        var optionalUser = userRepository.findByEmail(request.email());
        if (optionalUser.isEmpty()) {
            logger.warn("Login failed: Email not found: {}", request.email());
            return ApiResponse.error("Email not found");
        }

        User user = optionalUser.get();

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), request.password())
        );

        logger.info("Login successful for user: {}", user.getUsername());

        String token = jwtUtil.generateToken(user.getUsername(), 60 * 60 * 1000);
        String refreshToken = jwtUtil.generateToken(user.getUsername(), 7 * 24 * 60 * 60 * 1000);
        return ApiResponse.ok("Login successful", Map.of("token", token, "refreshToken", refreshToken));
    }
}
