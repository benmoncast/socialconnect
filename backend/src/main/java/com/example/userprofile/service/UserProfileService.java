package com.example.userprofile.service;

import com.example.userprofile.dto.UserProfileRequest;
import com.example.userprofile.dto.UserProfileResponse;
import java.util.List;

public interface UserProfileService {

    UserProfileResponse createUserProfile(UserProfileRequest request);

    List<UserProfileResponse> getAllUserProfiles();

    UserProfileResponse getUserProfileById(Long id);

    UserProfileResponse getUserProfileByUsername(String username);

    UserProfileResponse updateUserProfile(Long id, UserProfileRequest request);

    void deleteUserProfile(Long id);

    List<UserProfileResponse> searchUserProfiles(String keyword);
}
