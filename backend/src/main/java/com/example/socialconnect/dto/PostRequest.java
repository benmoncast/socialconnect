package com.example.socialconnect.dto;

import com.example.socialconnect.entity.PostPrivacy;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record PostRequest(
        @Size(max = 5000)
        String content,

        @Size(max = 500)
        @Pattern(
                regexp = "^$|^/uploads/[A-Za-z0-9._-]+\\.(?i:jpg|jpeg|png|webp)$",
                message = "Image URL must reference an uploaded JPG, JPEG, PNG, or WEBP image"
        )
        String imageUrl,

        PostPrivacy privacy
) {

    @AssertTrue(message = "Post content or image is required")
    public boolean hasContentOrImage() {
        return hasText(content) || hasText(imageUrl);
    }

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }
}
