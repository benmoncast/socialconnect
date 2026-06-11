package com.example.socialconnect.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.example.socialconnect.entity.FriendRequest;
import com.example.socialconnect.entity.FriendRequestStatus;
import com.example.socialconnect.entity.User;
import com.example.socialconnect.exception.AppException;
import com.example.socialconnect.repository.FriendRequestRepository;
import com.example.socialconnect.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
class FriendServiceAuthorizationTest {

    @Mock
    private FriendRequestRepository friendRequestRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CurrentUserService currentUserService;

    @Mock
    private NotificationService notificationService;

    @Mock
    private DtoMapper dtoMapper;

    @InjectMocks
    private FriendService friendService;

    @Test
    void acceptRequestRejectsUserWhoIsNotReceiver() {
        User sender = user(1L);
        User receiver = user(2L);
        User attacker = user(3L);
        FriendRequest request = request(sender, receiver);

        when(currentUserService.getCurrentUser()).thenReturn(attacker);
        when(friendRequestRepository.findById(50L)).thenReturn(Optional.of(request));

        assertThatThrownBy(() -> friendService.acceptRequest(50L))
                .isInstanceOfSatisfying(AppException.class, ex ->
                        assertThat(ex.getStatus()).isEqualTo(HttpStatus.FORBIDDEN));
    }

    @Test
    void rejectRequestRejectsUserWhoIsNotReceiver() {
        User sender = user(1L);
        User receiver = user(2L);
        User attacker = user(3L);
        FriendRequest request = request(sender, receiver);

        when(currentUserService.getCurrentUser()).thenReturn(attacker);
        when(friendRequestRepository.findById(50L)).thenReturn(Optional.of(request));

        assertThatThrownBy(() -> friendService.rejectRequest(50L))
                .isInstanceOfSatisfying(AppException.class, ex ->
                        assertThat(ex.getStatus()).isEqualTo(HttpStatus.FORBIDDEN));
    }

    private User user(Long id) {
        return User.builder().id(id).email("user" + id + "@example.test").username("user" + id).build();
    }

    private FriendRequest request(User sender, User receiver) {
        return FriendRequest.builder()
                .id(50L)
                .sender(sender)
                .receiver(receiver)
                .status(FriendRequestStatus.PENDING)
                .build();
    }
}
