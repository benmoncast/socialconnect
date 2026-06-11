package com.example.socialconnect.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record ProfileUpdateRequest(
        @NotBlank
        @Size(max = 100)
        String firstName,

        @Size(max = 100)
        String middleName,

        @NotBlank
        @Size(max = 100)
        String lastName,

        @NotBlank
        @Size(min = 3, max = 30)
        @Pattern(regexp = "^[A-Za-z0-9._-]+$", message = "Username can only contain letters, numbers, dots, underscores, and hyphens")
        String username,

        @Email
        @NotBlank
        @Size(max = 150)
        String email,

        @Size(max = 500)
        String bio,

        @Size(max = 50)
        String gender,

        @Past
        LocalDate birthdate,

        @Size(max = 30)
        @Pattern(regexp = "^[+0-9()\\-\\s]*$", message = "Phone number contains unsupported characters")
        String phoneNumber,

        @Size(max = 255)
        String address,

        @Size(max = 100)
        String city,

        @Size(max = 100)
        String province,

        @Size(max = 100)
        String country
) {
}
