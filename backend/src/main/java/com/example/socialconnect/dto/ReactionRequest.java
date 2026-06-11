package com.example.socialconnect.dto;

import com.example.socialconnect.entity.ReactionType;
import jakarta.validation.constraints.NotNull;

public record ReactionRequest(@NotNull ReactionType type) {
}
