package com.example.socialconnect.service;

import jakarta.servlet.http.HttpServletResponse;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
public class AuthCookieService {

    public static final String AUTH_COOKIE_NAME = "SOCIALCONNECT_AUTH";

    private final Duration maxAge;
    private final boolean secure;
    private final String sameSite;

    public AuthCookieService(
            @Value("${app.jwt.expiration}") long expirationMillis,
            @Value("${app.auth.cookie.secure:${app.security.require-https:true}}") boolean secure,
            @Value("${app.auth.cookie.same-site:Lax}") String sameSite
    ) {
        this.maxAge = Duration.ofMillis(expirationMillis);
        this.secure = secure;
        this.sameSite = sameSite;
    }

    public void addAuthCookie(HttpServletResponse response, String token) {
        response.addHeader(HttpHeaders.SET_COOKIE, baseCookie(token)
                .maxAge(maxAge)
                .build()
                .toString());
    }

    public void clearAuthCookie(HttpServletResponse response) {
        response.addHeader(HttpHeaders.SET_COOKIE, baseCookie("")
                .maxAge(Duration.ZERO)
                .build()
                .toString());
    }

    private ResponseCookie.ResponseCookieBuilder baseCookie(String value) {
        return ResponseCookie.from(AUTH_COOKIE_NAME, value)
                .httpOnly(true)
                .secure(secure)
                .sameSite(sameSite)
                .path("/");
    }
}
