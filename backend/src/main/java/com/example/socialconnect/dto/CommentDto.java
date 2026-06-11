package com.example.socialconnect.dto;

import java.time.LocalDateTime;

public record CommentDto(
        Long id,
        Long postId,
        UserDto author,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
