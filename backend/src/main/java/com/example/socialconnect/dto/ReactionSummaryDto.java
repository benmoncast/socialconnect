package com.example.socialconnect.dto;

import java.util.Map;

public record ReactionSummaryDto(long total, Map<String, Long> counts) {
}
