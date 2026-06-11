package com.example.socialconnect.service;

import com.example.socialconnect.exception.AppException;
import java.time.Duration;
import java.time.Instant;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class AuthAbuseProtectionService {

    private static final Duration ATTEMPT_WINDOW = Duration.ofMinutes(15);
    private static final Duration LOGIN_COOLDOWN = Duration.ofMinutes(15);
    private static final int MAX_REGISTER_ATTEMPTS_PER_CLIENT = 5;
    private static final int MAX_LOGIN_ATTEMPTS_PER_CLIENT = 20;
    private static final int MAX_LOGIN_ATTEMPTS_PER_IDENTIFIER = 10;
    private static final int MAX_FAILED_LOGINS_BEFORE_COOLDOWN = 5;

    private final ConcurrentMap<String, WindowCounter> counters = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, FailureTracker> loginFailures = new ConcurrentHashMap<>();

    public void checkRegisterAllowed(String clientKey) {
        incrementOrReject(
                "register:" + normalizeClientKey(clientKey),
                MAX_REGISTER_ATTEMPTS_PER_CLIENT,
                "Too many registration attempts. Please try again later."
        );
    }

    public void checkLoginAllowed(String identifier, String clientKey) {
        String normalizedIdentifier = normalizeIdentifier(identifier);
        rejectIfCoolingDown(normalizedIdentifier);
        incrementOrReject(
                "login-client:" + normalizeClientKey(clientKey),
                MAX_LOGIN_ATTEMPTS_PER_CLIENT,
                "Too many login attempts. Please try again later."
        );
        incrementOrReject(
                "login-identifier:" + normalizedIdentifier,
                MAX_LOGIN_ATTEMPTS_PER_IDENTIFIER,
                "Too many login attempts for this account. Please try again later."
        );
    }

    public void recordLoginFailure(String identifier) {
        String normalizedIdentifier = normalizeIdentifier(identifier);
        Instant now = Instant.now();
        loginFailures.compute(normalizedIdentifier, (key, tracker) -> {
            FailureTracker current = tracker == null || now.isAfter(tracker.windowExpiresAt)
                    ? new FailureTracker(0, now.plus(ATTEMPT_WINDOW), null)
                    : tracker;

            int failures = current.failures + 1;
            Instant lockedUntil = failures >= MAX_FAILED_LOGINS_BEFORE_COOLDOWN
                    ? now.plus(LOGIN_COOLDOWN)
                    : current.lockedUntil;

            return new FailureTracker(failures, current.windowExpiresAt, lockedUntil);
        });
    }

    public void recordLoginSuccess(String identifier) {
        loginFailures.remove(normalizeIdentifier(identifier));
    }

    private void rejectIfCoolingDown(String normalizedIdentifier) {
        FailureTracker tracker = loginFailures.get(normalizedIdentifier);
        if (tracker == null || tracker.lockedUntil == null) {
            return;
        }

        Instant now = Instant.now();
        if (now.isBefore(tracker.lockedUntil)) {
            throw new AppException(HttpStatus.TOO_MANY_REQUESTS, "Too many failed login attempts. Please try again later.");
        }
        loginFailures.remove(normalizedIdentifier);
    }

    private void incrementOrReject(String key, int maxAttempts, String message) {
        Instant now = Instant.now();
        WindowCounter counter = counters.compute(key, (ignored, existing) -> {
            if (existing == null || now.isAfter(existing.expiresAt)) {
                return new WindowCounter(1, now.plus(ATTEMPT_WINDOW));
            }
            return new WindowCounter(existing.count + 1, existing.expiresAt);
        });

        if (counter.count > maxAttempts) {
            throw new AppException(HttpStatus.TOO_MANY_REQUESTS, message);
        }
    }

    private String normalizeClientKey(String clientKey) {
        if (clientKey == null || clientKey.isBlank()) {
            return "unknown";
        }
        return clientKey.trim().toLowerCase(Locale.ROOT);
    }

    private String normalizeIdentifier(String identifier) {
        if (identifier == null || identifier.isBlank()) {
            return "unknown";
        }
        return identifier.trim().toLowerCase(Locale.ROOT);
    }

    private record WindowCounter(int count, Instant expiresAt) {
    }

    private record FailureTracker(int failures, Instant windowExpiresAt, Instant lockedUntil) {
    }
}
