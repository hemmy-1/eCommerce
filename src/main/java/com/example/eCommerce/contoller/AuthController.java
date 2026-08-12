package com.example.eCommerce.contoller;

import java.util.UUID;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.eCommerce.Dtos.LoginUserRequestDto;
import com.example.eCommerce.Dtos.RegUserRequestDto;
import com.example.eCommerce.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("register")
    public UUID register (@RequestBody RegUserRequestDto request ){
        return authService.register(request);
    }

    @PostMapping("login")
    public String login(@RequestBody LoginUserRequestDto request){
        return authService.login(request);
    }
    
    
}
