package com.example.socialconnect.dto;

import com.example.socialconnect.entity.PostPrivacy;
import com.example.socialconnect.entity.ReactionType;
import java.time.LocalDateTime;

public record PostDto(
        Long id,
        UserDto author,
        String content,
        String imageUrl,
        PostPrivacy privacy,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        long commentsCount,
        ReactionSummaryDto reactionSummary,
        ReactionType currentUserReaction
) {
}
