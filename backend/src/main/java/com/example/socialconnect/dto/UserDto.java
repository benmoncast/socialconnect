package com.example.socialconnect.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record UserDto(
        Long id,
        String firstName,
        String middleName,
        String lastName,
        String fullName,
        String username,
        String email,
        String bio,
        String profilePictureUrl,
        String coverPhotoUrl,
        String gender,
        LocalDate birthdate,
        String phoneNumber,
        String address,
        String city,
        String province,
        String country,
        String role,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String friendshipStatus
) {
}
