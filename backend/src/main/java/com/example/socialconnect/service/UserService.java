package com.example.socialconnect.service;

import com.example.socialconnect.dto.ProfileUpdateRequest;
import com.example.socialconnect.dto.UserDto;
import com.example.socialconnect.entity.User;
import com.example.socialconnect.exception.AppException;
import com.example.socialconnect.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final CurrentUserService currentUserService;
    private final FriendService friendService;
    private final UploadService uploadService;
    private final DtoMapper dtoMapper;

    @Transactional(readOnly = true)
    public UserDto getMe() {
        return dtoMapper.toUserDto(currentUserService.getCurrentUser(), "SELF");
    }

    public UserDto updateMe(ProfileUpdateRequest request) {
        User user = currentUserService.getCurrentUser();

        userRepository.findByUsername(request.username())
                .filter(existing -> !existing.getId().equals(user.getId()))
                .ifPresent(existing -> {
                    throw new AppException(HttpStatus.CONFLICT, "Username is already taken");
                });

        userRepository.findByEmail(request.email())
                .filter(existing -> !existing.getId().equals(user.getId()))
                .ifPresent(existing -> {
                    throw new AppException(HttpStatus.CONFLICT, "Email is already registered");
                });

        user.setFirstName(request.firstName());
        user.setMiddleName(request.middleName());
        user.setLastName(request.lastName());
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setBio(request.bio());
        user.setGender(request.gender());
        user.setBirthdate(request.birthdate());
        user.setPhoneNumber(request.phoneNumber());
        user.setAddress(request.address());
        user.setCity(request.city());
        user.setProvince(request.province());
        user.setCountry(request.country());

        return dtoMapper.toUserDto(userRepository.save(user), "SELF");
    }

    @Transactional(readOnly = true)
    public UserDto getUser(Long id) {
        User currentUser = currentUserService.getCurrentUser();
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "User not found"));
        return dtoMapper.toUserDto(user, friendService.friendshipStatus(currentUser, user));
    }

    @Transactional(readOnly = true)
    public List<UserDto> search(String query) {
        User currentUser = currentUserService.getCurrentUser();
        return userRepository.searchUsers(query == null ? "" : query.trim())
                .stream()
                .filter(user -> !user.getId().equals(currentUser.getId()))
                .map(user -> dtoMapper.toUserDto(user, friendService.friendshipStatus(currentUser, user)))
                .toList();
    }

    public UserDto updateProfilePicture(MultipartFile file) {
        User user = currentUserService.getCurrentUser();
        user.setProfilePictureUrl(uploadService.store(file));
        return dtoMapper.toUserDto(userRepository.save(user), "SELF");
    }

    public UserDto updateCoverPhoto(MultipartFile file) {
        User user = currentUserService.getCurrentUser();
        user.setCoverPhotoUrl(uploadService.store(file));
        return dtoMapper.toUserDto(userRepository.save(user), "SELF");
    }
}
