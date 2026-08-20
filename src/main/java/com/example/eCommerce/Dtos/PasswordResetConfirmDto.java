package com.example.eCommerce.Dtos;

import lombok.Data;

@Data
public class PasswordResetConfirmDto {
    
    private String token;
    
    private String newPassword;
}