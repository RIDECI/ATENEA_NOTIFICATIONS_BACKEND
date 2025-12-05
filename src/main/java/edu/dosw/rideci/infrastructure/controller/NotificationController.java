package edu.dosw.rideci.infrastructure.controller;

import edu.dosw.rideci.application.port.in.CreateEmergencyBroadcastUseCase;
import edu.dosw.rideci.application.port.in.FilterNotificationsUseCase;
import edu.dosw.rideci.application.port.in.GetCategoryNotificationsByDayUseCase;
import edu.dosw.rideci.application.port.in.GetRecentNotificationsUseCase;
import edu.dosw.rideci.application.port.in.SendEmailNotificationUseCase;
import edu.dosw.rideci.domain.model.AppNotification;
import edu.dosw.rideci.domain.model.Enum.Category;
import edu.dosw.rideci.infrastructure.controller.dto.NotificationDtoMapper;
import edu.dosw.rideci.infrastructure.controller.dto.Request.CreateNotificationRequest;
import edu.dosw.rideci.infrastructure.controller.dto.Request.EmailNotificationRequest;
import edu.dosw.rideci.infrastructure.controller.dto.Response.NotificationResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final CreateEmergencyBroadcastUseCase createEmergencyBroadcastUseCase;
    private final FilterNotificationsUseCase filterNotificationsUseCase;
    private final GetCategoryNotificationsByDayUseCase getCategoryNotificationsByDayUseCase;
    private final GetRecentNotificationsUseCase getRecentNotificationsUseCase;
    private final SendEmailNotificationUseCase sendEmailNotificationUseCase;

    @PostMapping("/emergency-broadcast")
    public ResponseEntity<List<NotificationResponse>> createEmergencyBroadcast(
            @Valid @RequestBody CreateNotificationRequest request) {

        CreateEmergencyBroadcastUseCase.CreateEmergencyBroadcastCommand command =
                new CreateEmergencyBroadcastUseCase.CreateEmergencyBroadcastCommand(
                        request.getMessage(),
                        request.getCategory(),
                        request.getTargetUserIds(),
                        request.getPriorityLevel()
                );

        List<AppNotification> created = createEmergencyBroadcastUseCase
                .createEmergencyBroadcast(command);

        List<NotificationResponse> responses = created.stream()
                .map(NotificationDtoMapper::toResponse)
                .toList();

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<NotificationResponse>> filterNotifications(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime startDate,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime endDate,
            @RequestParam(required = false) Category category,
            @RequestParam String userId) {

        FilterNotificationsUseCase.FilterNotificationsCommand command =
                new FilterNotificationsUseCase.FilterNotificationsCommand(
                        startDate, endDate, category, userId
                );

        List<AppNotification> notifications =
                filterNotificationsUseCase.filterNotifications(command);

        List<NotificationResponse> responses = notifications.stream()
                .map(NotificationDtoMapper::toResponse)
                .toList();

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/category-by-day")
    public ResponseEntity<List<NotificationResponse>> getCategoryNotificationsByDay(
            @RequestParam Category category,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date,
            @RequestParam String userId) {

        GetCategoryNotificationsByDayUseCase.GetCategoryNotificationsByDayCommand command =
                new GetCategoryNotificationsByDayUseCase.GetCategoryNotificationsByDayCommand(
                        category, date, userId
                );

        List<AppNotification> notifications =
                getCategoryNotificationsByDayUseCase.getCategoryNotificationsByDay(command);

        List<NotificationResponse> responses = notifications.stream()
                .map(NotificationDtoMapper::toResponse)
                .toList();

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/recent")
    public ResponseEntity<List<NotificationResponse>> getRecentNotifications(
            @RequestParam int limit,
            @RequestParam String userId) {

        GetRecentNotificationsUseCase.GetRecentNotificationsCommand command =
                new GetRecentNotificationsUseCase.GetRecentNotificationsCommand(limit, userId);

        List<AppNotification> notifications =
                getRecentNotificationsUseCase.getRecentNotifications(command);

        List<NotificationResponse> responses = notifications.stream()
                .map(NotificationDtoMapper::toResponse)
                .toList();

        return ResponseEntity.ok(responses);
    }

    @PostMapping("/email")
    public ResponseEntity<Void> sendEmailNotification(
            @Valid @RequestBody EmailNotificationRequest request) {

        sendEmailNotificationUseCase.sendEmail(
                UUID.fromString(request.getUserId()),
                request.getEmailType(),
                request.getTemplateData()
        );

        return ResponseEntity.accepted().build();
    }
}
