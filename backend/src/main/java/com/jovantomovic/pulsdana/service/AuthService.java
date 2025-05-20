package com.jovantomovic.pulsdana.service;

import com.jovantomovic.pulsdana.dto.AuthResponse;
import com.jovantomovic.pulsdana.dto.RegWithCredRequest;
import com.jovantomovic.pulsdana.exception.BadUserCredentialsException;
import com.jovantomovic.pulsdana.exception.InvalidTokenException;
import com.jovantomovic.pulsdana.exception.InvalidUserCredentialsException;
import com.jovantomovic.pulsdana.model.User;
import com.jovantomovic.pulsdana.repository.UserRepository;
import com.jovantomovic.pulsdana.security.CustomUserDetails;
import com.jovantomovic.pulsdana.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public AuthResponse loginWithCredentials(String usernameOrEmail, String password) {
        // Authenticate user
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(usernameOrEmail, password)
            );
        } catch (Exception ex) {
            throw new BadUserCredentialsException("Invalid username or password");
        }

        // Get authenticated user
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();

        // Generate tokens
        String accessToken = jwtUtil.generateAccessToken(user.getUsername());
        String refreshToken = jwtUtil.generateRefreshToken(user.getUsername());

        // Save refresh token to DB
        user.setRefreshToken(refreshToken);
        userRepository.save(user);

        return new AuthResponse(accessToken, refreshToken);
    }

    public void registerWithCredentials(RegWithCredRequest model) {
        // Email validation
        if (!Pattern.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", model.getEmail())) {
            throw new InvalidUserCredentialsException("Invalid email format");
        }

        // Password validation (at least 8 characters long and include uppercase, lowercase, number and special character)
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,}$";
        if (!Pattern.matches(passwordPattern, model.getPassword())) {
            throw new InvalidUserCredentialsException("Password must be at least 8 characters long and include uppercase, lowercase, number and special character");
        }

        // Check if username or email is already taken
        if (userRepository.findByUsername(model.getUsername()).isPresent()) {
            throw new InvalidUserCredentialsException("Username already taken");
        }
        if (userRepository.findByEmail(model.getEmail()).isPresent()) {
            throw new InvalidUserCredentialsException("Email already taken");
        }

        User user = new User();
        user.setUsername(model.getUsername());
        user.setEmail(model.getEmail());
        user.setPasswordHash(passwordEncoder.encode(model.getPassword()));
        user.setRoles(Set.of("USER"));
        userRepository.save(user);
    }

    public void logout(String username) {
        userRepository.findByUsername(username).ifPresent(user -> {
            user.setRefreshToken(null);
            userRepository.save(user);
        });
    }

    public String refreshAccessToken(String refreshToken) {
        // Validate refresh token
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new InvalidTokenException("Invalid refresh token");
        }

        // Extract username from refresh token
        String username = jwtUtil.extractUsername(refreshToken);

        // Get user with given username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new InvalidTokenException("Invalid refresh token"));

        // Compare refresh token with token from the DB
        if (!refreshToken.equals(user.getRefreshToken())) {
            throw new InvalidTokenException("Invalid refresh token");
        }

        return jwtUtil.generateAccessToken(username);
    }
}
