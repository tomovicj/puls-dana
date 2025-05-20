package com.jovantomovic.pulsdana.controller;

import com.jovantomovic.pulsdana.dto.*;
import com.jovantomovic.pulsdana.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login/credentials")
    public AuthResponse loginWithCredentials(@RequestBody LoginWithCredRequest request) {
            return authService.loginWithCredentials(
                    request.getUsernameOrEmail(),
                    request.getPassword()
            );
    }

    @PostMapping("/register/credentials")
    public AuthResponse registerWithCredentials(@RequestBody RegWithCredRequest request) {
        authService.registerWithCredentials(request);
        return authService.loginWithCredentials(request.getEmail(), request.getPassword());
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        authService.logout(authentication.getName());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh-token")
    public RefreshTokenResponse refreshAccessToken(@RequestBody RefreshTokenRequest request) {
        String newAccessToken = authService.refreshAccessToken(request.getRefreshToken());
        return new RefreshTokenResponse(newAccessToken);
    }
}
