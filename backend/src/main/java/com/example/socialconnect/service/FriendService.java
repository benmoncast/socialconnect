package com.example.socialconnect.service;

import com.example.socialconnect.dto.FriendRequestDto;
import com.example.socialconnect.dto.UserDto;
import com.example.socialconnect.entity.FriendRequest;
import com.example.socialconnect.entity.FriendRequestStatus;
import com.example.socialconnect.entity.NotificationType;
import com.example.socialconnect.entity.User;
import com.example.socialconnect.exception.AppException;
import com.example.socialconnect.repository.FriendRequestRepository;
import com.example.socialconnect.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class FriendService {

    private final FriendRequestRepository friendRequestRepository;
    private final UserRepository userRepository;
    private final CurrentUserService currentUserService;
    private final NotificationService notificationService;
    private final DtoMapper dtoMapper;

    public FriendRequestDto sendRequest(Long receiverId) {
        User currentUser = currentUserService.getCurrentUser();
        User receiver = getUser(receiverId);

        if (currentUser.getId().equals(receiver.getId())) {
            throw new AppException(HttpStatus.BAD_REQUEST, "You cannot send a friend request to yourself");
        }

        friendRequestRepository.findLatestBetweenUsers(currentUser, receiver).ifPresent(existing -> {
            if (existing.getStatus() == FriendRequestStatus.PENDING) {
                throw new AppException(HttpStatus.CONFLICT, "A friend request already exists");
            }
            if (existing.getStatus() == FriendRequestStatus.ACCEPTED) {
                throw new AppException(HttpStatus.CONFLICT, "You are already friends");
            }
        });

        FriendRequest request = friendRequestRepository.save(FriendRequest.builder()
                .sender(currentUser)
                .receiver(receiver)
                .status(FriendRequestStatus.PENDING)
                .build());

        notificationService.notify(receiver, currentUser, NotificationType.FRIEND_REQUEST,
                dtoMapper.fullName(currentUser) + " sent you a friend request.");
        return dtoMapper.toFriendRequestDto(request);
    }

    public void cancelRequest(Long receiverId) {
        User currentUser = currentUserService.getCurrentUser();
        User receiver = getUser(receiverId);
        FriendRequest request = friendRequestRepository.findBySenderAndReceiver(currentUser, receiver)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Friend request not found"));

        if (request.getStatus() != FriendRequestStatus.PENDING) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Only pending requests can be cancelled");
        }
        friendRequestRepository.delete(request);
    }

    public FriendRequestDto acceptRequest(Long requestId) {
        User currentUser = currentUserService.getCurrentUser();
        FriendRequest request = getRequest(requestId);

        if (!request.getReceiver().getId().equals(currentUser.getId())) {
            throw new AppException(HttpStatus.FORBIDDEN, "You can only accept requests sent to you");
        }

        request.setStatus(FriendRequestStatus.ACCEPTED);
        FriendRequest saved = friendRequestRepository.save(request);
        notificationService.notify(request.getSender(), currentUser, NotificationType.FRIEND_ACCEPTED,
                dtoMapper.fullName(currentUser) + " accepted your friend request.");
        return dtoMapper.toFriendRequestDto(saved);
    }

    public FriendRequestDto rejectRequest(Long requestId) {
        User currentUser = currentUserService.getCurrentUser();
        FriendRequest request = getRequest(requestId);

        if (!request.getReceiver().getId().equals(currentUser.getId())) {
            throw new AppException(HttpStatus.FORBIDDEN, "You can only reject requests sent to you");
        }

        request.setStatus(FriendRequestStatus.REJECTED);
        return dtoMapper.toFriendRequestDto(friendRequestRepository.save(request));
    }

    public void unfriend(Long friendId) {
        User currentUser = currentUserService.getCurrentUser();
        User friend = getUser(friendId);
        FriendRequest request = friendRequestRepository
                .findBetweenUsersWithStatus(currentUser, friend, FriendRequestStatus.ACCEPTED)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Friendship not found"));
        friendRequestRepository.delete(request);
    }

    @Transactional(readOnly = true)
    public List<UserDto> getFriends() {
        User currentUser = currentUserService.getCurrentUser();
        return friendRequestRepository.findFriends(currentUser)
                .stream()
                .map(friend -> dtoMapper.toUserDto(friend, "FRIENDS"))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<FriendRequestDto> getReceivedRequests() {
        return friendRequestRepository
                .findByReceiverAndStatusOrderByCreatedAtDesc(currentUserService.getCurrentUser(), FriendRequestStatus.PENDING)
                .stream()
                .map(dtoMapper::toFriendRequestDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<FriendRequestDto> getSentRequests() {
        return friendRequestRepository
                .findBySenderAndStatusOrderByCreatedAtDesc(currentUserService.getCurrentUser(), FriendRequestStatus.PENDING)
                .stream()
                .map(dtoMapper::toFriendRequestDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public String friendshipStatus(User currentUser, User otherUser) {
        if (currentUser.getId().equals(otherUser.getId())) {
            return "SELF";
        }

        return friendRequestRepository.findLatestBetweenUsers(currentUser, otherUser)
                .map(request -> {
                    if (request.getStatus() == FriendRequestStatus.ACCEPTED) {
                        return "FRIENDS";
                    }
                    if (request.getStatus() == FriendRequestStatus.PENDING
                            && request.getSender().getId().equals(currentUser.getId())) {
                        return "REQUEST_SENT";
                    }
                    if (request.getStatus() == FriendRequestStatus.PENDING) {
                        return "RESPOND_TO_REQUEST";
                    }
                    return "ADD_FRIEND";
                })
                .orElse("ADD_FRIEND");
    }

    @Transactional(readOnly = true)
    public String friendshipStatus(Long userId) {
        return friendshipStatus(currentUserService.getCurrentUser(), getUser(userId));
    }

    @Transactional(readOnly = true)
    public boolean areFriends(User firstUser, User secondUser) {
        return friendRequestRepository
                .findBetweenUsersWithStatus(firstUser, secondUser, FriendRequestStatus.ACCEPTED)
                .isPresent();
    }

    private User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "User not found"));
    }

    private FriendRequest getRequest(Long id) {
        return friendRequestRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Friend request not found"));
    }
}
