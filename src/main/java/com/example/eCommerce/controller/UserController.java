package com.example.eCommerce.controller;

import com.example.eCommerce.Dtos.LoginUserRequestDto;
import com.example.eCommerce.Dtos.UserResponseDto;
import com.example.eCommerce.Exception.UsernameNotFoundException;
import com.example.eCommerce.entity.User;
import com.example.eCommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @PostMapping("/me")
    public ResponseEntity<UserResponseDto> getCurrentUser(@RequestBody LoginUserRequestDto credential) {
        User currentUser = userRepository.findByEmail(credential.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return ResponseEntity.ok(new UserResponseDto(
                currentUser.getId(),
                currentUser.getNickName(),
                currentUser.getEmail(),
                currentUser.getRole().name()));
    }
}