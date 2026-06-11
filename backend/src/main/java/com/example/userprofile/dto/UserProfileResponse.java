package com.example.userprofile.dto;

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
public class UserProfileResponse {

    private Long id;
    private Long userId;
    private String firstName;
    private String middleName;
    private String lastName;
    private String username;
    private String bio;
    private String profilePictureUrl;
    private String coverPhotoUrl;
    private String gender;
    private LocalDate birthdate;
    private String phoneNumber;
    private String address;
    private String city;
    private String province;
    private String country;
}
