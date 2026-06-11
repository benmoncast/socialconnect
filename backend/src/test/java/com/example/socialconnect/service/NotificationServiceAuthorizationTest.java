package com.example.socialconnect.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.example.socialconnect.entity.Notification;
import com.example.socialconnect.entity.NotificationType;
import com.example.socialconnect.entity.User;
import com.example.socialconnect.exception.AppException;
import com.example.socialconnect.repository.NotificationRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
class NotificationServiceAuthorizationTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private CurrentUserService currentUserService;

    @Mock
    private DtoMapper dtoMapper;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    void markReadRejectsNonRecipient() {
        User recipient = user(1L);
        User actor = user(2L);
        User attacker = user(3L);
        Notification notification = Notification.builder()
                .id(70L)
                .recipient(recipient)
                .actor(actor)
                .type(NotificationType.FRIEND_REQUEST)
                .message("message")
                .build();

        when(currentUserService.getCurrentUser()).thenReturn(attacker);
        when(notificationRepository.findById(70L)).thenReturn(Optional.of(notification));

        assertThatThrownBy(() -> notificationService.markRead(70L))
                .isInstanceOfSatisfying(AppException.class, ex ->
                        assertThat(ex.getStatus()).isEqualTo(HttpStatus.FORBIDDEN));
    }

    private User user(Long id) {
        return User.builder().id(id).email("user" + id + "@example.test").username("user" + id).build();
    }
}
