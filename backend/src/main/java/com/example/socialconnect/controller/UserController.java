package com.example.socialconnect.controller;

import com.example.socialconnect.dto.ProfileUpdateRequest;
import com.example.socialconnect.dto.UserDto;
import com.example.socialconnect.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserDto> getMe() {
        return ResponseEntity.ok(userService.getMe());
    }

    @PutMapping("/me")
    public ResponseEntity<UserDto> updateMe(@Valid @RequestBody ProfileUpdateRequest request) {
        return ResponseEntity.ok(userService.updateMe(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserDto>> search(@RequestParam(defaultValue = "") String query) {
        return ResponseEntity.ok(userService.search(query));
    }

    @PostMapping("/me/profile-picture")
    public ResponseEntity<UserDto> uploadProfilePicture(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(userService.updateProfilePicture(file));
    }

    @PostMapping("/me/cover-photo")
    public ResponseEntity<UserDto> uploadCoverPhoto(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(userService.updateCoverPhoto(file));
    }
}
