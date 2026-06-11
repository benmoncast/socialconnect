package com.example.socialconnect.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public record ProfileUpdateRequest(
        @NotBlank String firstName,
        String middleName,
        @NotBlank String lastName,
        @NotBlank String username,
        @Email @NotBlank String email,
        String bio,
        String gender,
        LocalDate birthdate,
        String phoneNumber,
        String address,
        String city,
        String province,
        String country
) {
}
