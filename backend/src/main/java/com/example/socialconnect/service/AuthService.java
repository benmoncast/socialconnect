package com.example.socialconnect.service;

import com.example.socialconnect.dto.AuthResponse;
import com.example.socialconnect.dto.LoginRequest;
import com.example.socialconnect.dto.RegisterRequest;
import com.example.socialconnect.entity.Role;
import com.example.socialconnect.entity.User;
import com.example.socialconnect.exception.AppException;
import com.example.socialconnect.repository.UserRepository;
import com.example.socialconnect.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CurrentUserService currentUserService;
    private final DtoMapper dtoMapper;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new AppException(HttpStatus.CONFLICT, "Username is already taken");
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new AppException(HttpStatus.CONFLICT, "Email is already registered");
        }

        User user = userRepository.save(User.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .gender(request.gender())
                .birthdate(request.birthdate())
                .role(Role.USER)
                .build());

        return new AuthResponse(jwtService.generateToken(user), dtoMapper.toUserDto(user, "SELF"));
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmailOrUsername(request.identifier(), request.identifier())
                .orElseThrow(() -> new AppException(HttpStatus.UNAUTHORIZED, "Invalid email, username, or password"));

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(), request.password())
        );

        return new AuthResponse(jwtService.generateToken(user), dtoMapper.toUserDto(user, "SELF"));
    }

    @Transactional(readOnly = true)
    public AuthResponse me() {
        User user = currentUserService.getCurrentUser();
        return new AuthResponse(null, dtoMapper.toUserDto(user, "SELF"));
    }
}
