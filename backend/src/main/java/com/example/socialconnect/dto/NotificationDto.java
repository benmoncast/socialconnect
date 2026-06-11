package com.example.socialconnect.dto;

import com.example.socialconnect.entity.NotificationType;
import java.time.LocalDateTime;

public record NotificationDto(
        Long id,
        UserDto actor,
        NotificationType type,
        String message,
        boolean read,
        LocalDateTime createdAt
) {
}
