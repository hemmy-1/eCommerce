package com.example.eCommerce.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.eCommerce.Dtos.LoginUserRequestDto;
import com.example.eCommerce.Dtos.RegUserRequestDto;
import com.example.eCommerce.entity.User;
import com.example.eCommerce.repository.UserRepository;

@Service
public class AuthService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JwtService jwtService;
    private AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService,
            AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public UUID register(RegUserRequestDto request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("this user already exist");
        }

        User newUser = new User();

        newUser.setEmail(request.getEmail());
        newUser.setNickName(request.getNickName());

        String hashedPassword = passwordEncoder.encode(request.getPassword());

        newUser.setPassword(hashedPassword);
        newUser.setTime(LocalDateTime.now());

        userRepository.save(newUser);

        return newUser.getId();
    }

    public String login(LoginUserRequestDto request) {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            String token = jwtService.generateToken(userDetails);

            return token;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Username or PAssword Not correct");
        }
    }



}
