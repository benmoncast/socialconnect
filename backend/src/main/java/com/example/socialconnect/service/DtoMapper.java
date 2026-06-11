package com.example.socialconnect.service;

import com.example.socialconnect.dto.CommentDto;
import com.example.socialconnect.dto.FriendRequestDto;
import com.example.socialconnect.dto.NotificationDto;
import com.example.socialconnect.dto.PostDto;
import com.example.socialconnect.dto.ReactionSummaryDto;
import com.example.socialconnect.dto.UserDto;
import com.example.socialconnect.entity.Comment;
import com.example.socialconnect.entity.FriendRequest;
import com.example.socialconnect.entity.Notification;
import com.example.socialconnect.entity.Post;
import com.example.socialconnect.entity.Reaction;
import com.example.socialconnect.entity.ReactionType;
import com.example.socialconnect.entity.User;
import com.example.socialconnect.repository.CommentRepository;
import com.example.socialconnect.repository.ReactionRepository;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DtoMapper {

    private final CommentRepository commentRepository;
    private final ReactionRepository reactionRepository;

    public UserDto toUserDto(User user, String friendshipStatus) {
        return new UserDto(
                user.getId(),
                user.getFirstName(),
                user.getMiddleName(),
                user.getLastName(),
                fullName(user),
                user.getUsername(),
                user.getEmail(),
                user.getBio(),
                user.getProfilePictureUrl(),
                user.getCoverPhotoUrl(),
                user.getGender(),
                user.getBirthdate(),
                user.getPhoneNumber(),
                user.getAddress(),
                user.getCity(),
                user.getProvince(),
                user.getCountry(),
                user.getRole().name(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                friendshipStatus
        );
    }

    public PostDto toPostDto(Post post, User currentUser, String friendshipStatus) {
        Optional<Reaction> currentReaction = reactionRepository.findByPostAndUser(post, currentUser);
        return new PostDto(
                post.getId(),
                toUserDto(post.getUser(), friendshipStatus),
                post.getContent(),
                post.getImageUrl(),
                post.getPrivacy(),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                commentRepository.countByPost(post),
                reactionSummary(post),
                currentReaction.map(Reaction::getType).orElse(null)
        );
    }

    public CommentDto toCommentDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getPost().getId(),
                toUserDto(comment.getUser(), null),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }

    public FriendRequestDto toFriendRequestDto(FriendRequest request) {
        return new FriendRequestDto(
                request.getId(),
                toUserDto(request.getSender(), null),
                toUserDto(request.getReceiver(), null),
                request.getStatus(),
                request.getCreatedAt(),
                request.getUpdatedAt()
        );
    }

    public NotificationDto toNotificationDto(Notification notification) {
        return new NotificationDto(
                notification.getId(),
                toUserDto(notification.getActor(), null),
                notification.getType(),
                notification.getMessage(),
                notification.isRead(),
                notification.getCreatedAt()
        );
    }

    public String fullName(User user) {
        return (user.getFirstName() + " " + user.getLastName()).trim();
    }

    private ReactionSummaryDto reactionSummary(Post post) {
        Map<String, Long> counts = Arrays.stream(ReactionType.values())
                .collect(Collectors.toMap(Enum::name, type -> reactionRepository.countByPostAndType(post, type)));
        long total = counts.values().stream().mapToLong(Long::longValue).sum();
        return new ReactionSummaryDto(total, counts);
    }
}
