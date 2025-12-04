package edu.dosw.rideci.infrastructure.controller;

import edu.dosw.rideci.application.port.in.CreateNotificationUseCase;
import edu.dosw.rideci.application.port.in.GetUserNotificationsUseCase;
import edu.dosw.rideci.application.port.in.MarkNotificationAsReadUseCase;
import edu.dosw.rideci.infrastructure.controller.dto.NotificationDtoMapper;
import edu.dosw.rideci.infrastructure.controller.dto.Request.CreateNotificationRequest;
import edu.dosw.rideci.infrastructure.controller.dto.Response.NotificationResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final CreateNotificationUseCase createNotificationUseCase;
    private final GetUserNotificationsUseCase getUserNotificationsUseCase;
    private final MarkNotificationAsReadUseCase markNotificationAsReadUseCase;

    @PostMapping
    public ResponseEntity<NotificationResponse> createNotification(
            @Valid @RequestBody CreateNotificationRequest request) {

        InAppNotification domain = NotificationDtoMapper.toDomain(request);
        InAppNotification created = createNotificationUseCase.createNotification(domain);
        NotificationResponse response = NotificationDtoMapper.toResponse(created);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationResponse>> getUserNotifications(
            @PathVariable UUID userId) {

        List<InAppNotification> notifications =
                getUserNotificationsUseCase.getNotificationsByUserId(userId);

        List<NotificationResponse> responses = notifications.stream()
                .map(NotificationDtoMapper::toResponse)
                .toList();

        return ResponseEntity.ok(responses);
    }

    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<NotificationResponse> markAsRead(
            @PathVariable UUID notificationId) {

        InAppNotification updated = markNotificationAsReadUseCase.markAsRead(notificationId);
        NotificationResponse response = NotificationDtoMapper.toResponse(updated);

        return ResponseEntity.ok(response);
    }
}
