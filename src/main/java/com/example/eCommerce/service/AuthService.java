package com.example.eCommerce.service;

import com.example.eCommerce.Dtos.*;
import com.example.eCommerce.entity.User;
import com.example.eCommerce.enums.Role;
import com.example.eCommerce.repository.UserRepository;
import com.example.eCommerce.Exception.IllegalArgumentException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final JavaMailSender mailSender;

    @Transactional
    public String register(RegUserRequestDto request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Registration request received.");
        }

        User newUser = new User();
        newUser.setEmail(request.getEmail());
        newUser.setNickName(request.getNickName());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setRole(Role.ROLE_CUSTOMER);
        newUser.setVerified(false);

        String verificationCode = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        newUser.setVerificationCode(verificationCode);

        userRepository.save(newUser);

        sendEmail(newUser.getEmail(), "Verify Your Email",
                "Welcome to eCommerce! Your verification code is: " + verificationCode);

        return "Registration successful. Please check your email for the verification code.";
    }

    @Transactional
    public void verifyEmail(String email, String code) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid verification request"));

        if (user.getVerificationCode() == null || !code.equals(user.getVerificationCode())) {
            throw new IllegalArgumentException("Invalid verification code");
        }

        user.setVerified(true);
        user.setVerificationCode(null);
        userRepository.save(user);
    }

    @Transactional
    public AuthResponseDto login(LoginUserRequestDto request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        } catch (Exception ex) {
            throw new BadCredentialsException("Invalid email or password");
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        String accessToken = jwtService.generateToken(user);
        String refreshToken = UUID.randomUUID().toString();

        user.setRefreshToken(refreshToken);
        userRepository.save(user);

        return new AuthResponseDto(accessToken, refreshToken);
    }

    @Transactional
    public AuthResponseDto refreshToken(RefreshTokenRequestDto request) {
        User user = userRepository.findByRefreshToken(request.getRefreshToken())
                .orElseThrow(() -> new IllegalArgumentException("Invalid or expired refresh token"));

        String newAccessToken = jwtService.generateToken(user);
        return new AuthResponseDto(newAccessToken, user.getRefreshToken());
    }

    @Transactional
    public void logout(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user != null) {
            user.setRefreshToken(null);
            userRepository.save(user);
        }
    }

    @Transactional
    public String requestPasswordReset(PasswordResetRequestDto request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid request"));

        String token = UUID.randomUUID().toString();
        user.setPasswordResetToken(token);
        user.setPasswordResetExpiry(LocalDateTime.now().plusMinutes(15));
        userRepository.save(user);

        sendEmail(user.getEmail(), "Password Reset Request",
                "To reset your password, use the token below:\n" + token);

        return "If an account exists with this email, a reset link has been sent.";
    }

    @Transactional
    public void confirmPasswordReset(PasswordResetConfirmDto request) {
        User user = userRepository.findByPasswordResetToken(request.getToken())
                .orElseThrow(() -> new IllegalArgumentException("Invalid or expired reset token"));

        if (user.getPasswordResetExpiry().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Reset token has expired");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setPasswordResetToken(null);
        user.setPasswordResetExpiry(null);
        userRepository.save(user);
    }

    private void sendEmail(String toEmail, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}