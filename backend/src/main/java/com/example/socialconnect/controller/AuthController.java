package com.example.socialconnect.controller;

import com.example.socialconnect.dto.AuthResponse;
import com.example.socialconnect.dto.LoginRequest;
import com.example.socialconnect.dto.RegisterRequest;
import com.example.socialconnect.service.AuthCookieService;
import com.example.socialconnect.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final AuthCookieService authCookieService;

    @GetMapping("/csrf")
    public ResponseEntity<Map<String, String>> csrf(CsrfToken csrfToken) {
        return ResponseEntity.ok(Map.of(
                "headerName", csrfToken.getHeaderName(),
                "token", csrfToken.getToken()
        ));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @Valid @RequestBody RegisterRequest request,
            HttpServletRequest servletRequest,
            HttpServletResponse servletResponse
    ) {
        AuthResponse authResponse = authService.register(request, clientKey(servletRequest));
        authCookieService.addAuthCookie(servletResponse, authResponse.token());
        return ResponseEntity.status(HttpStatus.CREATED).body(new AuthResponse(null, authResponse.user()));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest servletRequest,
            HttpServletResponse servletResponse
    ) {
        AuthResponse authResponse = authService.login(request, clientKey(servletRequest));
        authCookieService.addAuthCookie(servletResponse, authResponse.token());
        return ResponseEntity.ok(new AuthResponse(null, authResponse.user()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse servletResponse) {
        SecurityContextHolder.clearContext();
        authCookieService.clearAuthCookie(servletResponse);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<AuthResponse> me() {
        return ResponseEntity.ok(authService.me());
    }

    private String clientKey(HttpServletRequest request) {
        return request.getRemoteAddr();
    }
}
