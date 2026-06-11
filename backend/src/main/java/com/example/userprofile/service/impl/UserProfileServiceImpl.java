package com.example.userprofile.service.impl;

import com.example.userprofile.dto.UserProfileRequest;
import com.example.userprofile.dto.UserProfileResponse;
import com.example.userprofile.entity.UserProfile;
import com.example.userprofile.exception.DuplicateResourceException;
import com.example.userprofile.exception.ResourceNotFoundException;
import com.example.userprofile.repository.UserProfileRepository;
import com.example.userprofile.service.UserIdGenerationService;
import com.example.userprofile.service.UserProfileService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional
public class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserIdGenerationService userIdGenerationService;

    @Override
    public UserProfileResponse createUserProfile(UserProfileRequest request) {
        validateUniqueUsername(request.getUsername());

        UserProfile profile = toEntity(request);
        profile.setUserId(userIdGenerationService.generateUserId());

        UserProfile savedProfile = userProfileRepository.save(profile);
        return toResponse(savedProfile);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserProfileResponse> getAllUserProfiles() {
        return userProfileRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UserProfileResponse getUserProfileById(Long id) {
        return toResponse(findProfileById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public UserProfileResponse getUserProfileByUsername(String username) {
        UserProfile profile = userProfileRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User profile not found with username: " + username));
        return toResponse(profile);
    }

    @Override
    public UserProfileResponse updateUserProfile(Long id, UserProfileRequest request) {
        UserProfile profile = findProfileById(id);

        if (userProfileRepository.existsByUsernameAndIdNot(request.getUsername(), id)) {
            throw new DuplicateResourceException("Username already exists: " + request.getUsername());
        }

        applyRequest(profile, request);
        return toResponse(userProfileRepository.save(profile));
    }

    @Override
    public void deleteUserProfile(Long id) {
        UserProfile profile = findProfileById(id);
        userProfileRepository.delete(profile);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserProfileResponse> searchUserProfiles(String keyword) {
        if (!StringUtils.hasText(keyword)) {
            return getAllUserProfiles();
        }

        return userProfileRepository.search(keyword.trim())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private void validateUniqueUsername(String username) {
        if (userProfileRepository.existsByUsername(username)) {
            throw new DuplicateResourceException("Username already exists: " + username);
        }
    }

    private UserProfile findProfileById(Long id) {
        return userProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User profile not found with ID: " + id));
    }

    private UserProfile toEntity(UserProfileRequest request) {
        UserProfile profile = new UserProfile();
        applyRequest(profile, request);
        return profile;
    }

    private void applyRequest(UserProfile profile, UserProfileRequest request) {
        profile.setFirstName(request.getFirstName());
        profile.setMiddleName(request.getMiddleName());
        profile.setLastName(request.getLastName());
        profile.setUsername(request.getUsername());
        profile.setBio(request.getBio());
        profile.setProfilePictureUrl(request.getProfilePictureUrl());
        profile.setCoverPhotoUrl(request.getCoverPhotoUrl());
        profile.setGender(request.getGender());
        profile.setBirthdate(request.getBirthdate());
        profile.setPhoneNumber(request.getPhoneNumber());
        profile.setAddress(request.getAddress());
        profile.setCity(request.getCity());
        profile.setProvince(request.getProvince());
        profile.setCountry(request.getCountry());
    }

    private UserProfileResponse toResponse(UserProfile profile) {
        return UserProfileResponse.builder()
                .id(profile.getId())
                .userId(profile.getUserId())
                .firstName(profile.getFirstName())
                .middleName(profile.getMiddleName())
                .lastName(profile.getLastName())
                .username(profile.getUsername())
                .bio(profile.getBio())
                .profilePictureUrl(profile.getProfilePictureUrl())
                .coverPhotoUrl(profile.getCoverPhotoUrl())
                .gender(profile.getGender())
                .birthdate(profile.getBirthdate())
                .phoneNumber(profile.getPhoneNumber())
                .address(profile.getAddress())
                .city(profile.getCity())
                .province(profile.getProvince())
                .country(profile.getCountry())
                .build();
    }
}
