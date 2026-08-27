package com.example.eCommerce.Dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CreateReviewRequestDto(
        @NotNull @Min(1) @Max(5) Integer rating,
        @NotBlank String comment) {
}