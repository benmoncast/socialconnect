package com.example.socialconnect.service;

import com.example.socialconnect.dto.NotificationDto;
import com.example.socialconnect.entity.Notification;
import com.example.socialconnect.entity.NotificationType;
import com.example.socialconnect.entity.User;
import com.example.socialconnect.exception.AppException;
import com.example.socialconnect.repository.NotificationRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final CurrentUserService currentUserService;
    private final DtoMapper dtoMapper;

    public void notify(User recipient, User actor, NotificationType type, String message) {
        if (recipient.getId().equals(actor.getId())) {
            return;
        }

        notificationRepository.save(Notification.builder()
                .recipient(recipient)
                .actor(actor)
                .type(type)
                .message(message)
                .read(false)
                .build());
    }

    @Transactional(readOnly = true)
    public List<NotificationDto> getNotifications() {
        User currentUser = currentUserService.getCurrentUser();
        return notificationRepository.findByRecipientOrderByCreatedAtDesc(currentUser)
                .stream()
                .map(dtoMapper::toNotificationDto)
                .toList();
    }

    public NotificationDto markRead(Long id) {
        User currentUser = currentUserService.getCurrentUser();
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Notification not found"));

        if (!notification.getRecipient().getId().equals(currentUser.getId())) {
            throw new AppException(HttpStatus.FORBIDDEN, "You can only update your own notifications");
        }

        notification.setRead(true);
        return dtoMapper.toNotificationDto(notificationRepository.save(notification));
    }

    public void markAllRead() {
        User currentUser = currentUserService.getCurrentUser();
        notificationRepository.findByRecipientOrderByCreatedAtDesc(currentUser).forEach(notification -> {
            notification.setRead(true);
            notificationRepository.save(notification);
        });
    }
}
