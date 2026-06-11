package com.example.socialconnect.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record RegisterRequest(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank String username,
        @Email @NotBlank String email,
        @Size(min = 6, message = "Password must be at least 6 characters") String password,
        String gender,
        LocalDate birthdate
) {
}
