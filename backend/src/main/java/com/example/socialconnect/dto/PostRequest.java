package com.example.socialconnect.dto;

import com.example.socialconnect.entity.PostPrivacy;

public record PostRequest(String content, String imageUrl, PostPrivacy privacy) {
}
