package com.example.userprofile.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileRequest {

    @NotBlank(message = "First name is required")
    @Size(max = 100, message = "First name must be 100 characters or fewer")
    private String firstName;

    @Size(max = 100, message = "Middle name must be 100 characters or fewer")
    private String middleName;

    @NotBlank(message = "Last name is required")
    @Size(max = 100, message = "Last name must be 100 characters or fewer")
    private String lastName;

    @NotBlank(message = "Username is required")
    @Size(max = 100, message = "Username must be 100 characters or fewer")
    private String username;

    private String bio;

    @Size(max = 500, message = "Profile picture URL must be 500 characters or fewer")
    private String profilePictureUrl;

    @Size(max = 500, message = "Cover photo URL must be 500 characters or fewer")
    private String coverPhotoUrl;

    @Size(max = 50, message = "Gender must be 50 characters or fewer")
    private String gender;

    private LocalDate birthdate;

    @Size(max = 30, message = "Phone number must be 30 characters or fewer")
    private String phoneNumber;

    @Size(max = 255, message = "Address must be 255 characters or fewer")
    private String address;

    @Size(max = 100, message = "City must be 100 characters or fewer")
    private String city;

    @Size(max = 100, message = "Province must be 100 characters or fewer")
    private String province;

    @Size(max = 100, message = "Country must be 100 characters or fewer")
    private String country;
}
