package com.example.eCommerce.controller;

import com.example.eCommerce.Dtos.*;
import com.example.eCommerce.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegUserRequestDto request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestBody VerifyEmailRequestDto verify) {
        authService.verifyEmail(verify.getEmail(), verify.getCode());
        return ResponseEntity.ok("Email verified successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginUserRequestDto request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponseDto> refreshToken(@RequestBody RefreshTokenRequestDto request) {
        return ResponseEntity.ok(authService.refreshToken(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody String email) {
        authService.logout(email);
        return ResponseEntity.ok("Logged out successfully");
    }

    @PostMapping("/password-reset/request")
    public ResponseEntity<String> requestReset(@RequestBody PasswordResetRequestDto request) {
        return ResponseEntity.ok(authService.requestPasswordReset(request));
    }

    @PostMapping("/password-reset/confirm")
    public ResponseEntity<String> confirmReset(@RequestBody PasswordResetConfirmDto request) {
        authService.confirmPasswordReset(request);
        return ResponseEntity.ok("Password reset successfully");
    }
}