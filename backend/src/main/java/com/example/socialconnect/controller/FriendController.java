package com.example.socialconnect.controller;

import com.example.socialconnect.dto.FriendRequestDto;
import com.example.socialconnect.dto.UserDto;
import com.example.socialconnect.service.FriendService;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/friends")
public class FriendController {

    private final FriendService friendService;

    @PostMapping("/request/{receiverId}")
    public ResponseEntity<FriendRequestDto> sendRequest(@PathVariable Long receiverId) {
        return ResponseEntity.ok(friendService.sendRequest(receiverId));
    }

    @DeleteMapping("/request/{receiverId}/cancel")
    public ResponseEntity<Void> cancelRequest(@PathVariable Long receiverId) {
        friendService.cancelRequest(receiverId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/request/{requestId}/accept")
    public ResponseEntity<FriendRequestDto> acceptRequest(@PathVariable Long requestId) {
        return ResponseEntity.ok(friendService.acceptRequest(requestId));
    }

    @PostMapping("/request/{requestId}/reject")
    public ResponseEntity<FriendRequestDto> rejectRequest(@PathVariable Long requestId) {
        return ResponseEntity.ok(friendService.rejectRequest(requestId));
    }

    @DeleteMapping("/{friendId}")
    public ResponseEntity<Void> unfriend(@PathVariable Long friendId) {
        friendService.unfriend(friendId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> friends() {
        return ResponseEntity.ok(friendService.getFriends());
    }

    @GetMapping("/requests/received")
    public ResponseEntity<List<FriendRequestDto>> receivedRequests() {
        return ResponseEntity.ok(friendService.getReceivedRequests());
    }

    @GetMapping("/requests/sent")
    public ResponseEntity<List<FriendRequestDto>> sentRequests() {
        return ResponseEntity.ok(friendService.getSentRequests());
    }

    @GetMapping("/status/{userId}")
    public ResponseEntity<Map<String, String>> status(@PathVariable Long userId) {
        return ResponseEntity.ok(Map.of("status", friendService.friendshipStatus(userId)));
    }
}
