package com.example.socialconnect.dto;

import com.example.socialconnect.entity.FriendRequestStatus;
import java.time.LocalDateTime;

public record FriendRequestDto(
        Long id,
        UserDto sender,
        UserDto receiver,
        FriendRequestStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
