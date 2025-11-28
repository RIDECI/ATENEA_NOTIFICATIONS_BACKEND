package edu.dosw.rideci.infrastructure.controller;

import edu.dosw.rideci.application.port.in.CreateNotificationUseCase;
import edu.dosw.rideci.application.port.in.GetUserNotificationsUseCase;
import edu.dosw.rideci.application.port.in.MarkNotificationAsReadUseCase;
import edu.dosw.rideci.domain.model.InAppNotification;
import edu.dosw.rideci.infrastructure.controller.dto.NotificationDtoMapper;
import edu.dosw.rideci.infrastructure.controller.dto.Request.CreateNotificationRequest;
import edu.dosw.rideci.infrastructure.controller.dto.Response.NotificationResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Controlador REST para la gestión de notificaciones.
 * Expone endpoints para crear notificaciones, consultar por usuario
 * y marcar una notificación como leída.
 *
 * Orquesta los casos de uso de notificaciones y realiza el mapeo
 * entre DTOs y el modelo de dominio.
 *
 * @author RideECI
 * @version 1.0
 */
@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final CreateNotificationUseCase createNotificationUseCase;
    private final GetUserNotificationsUseCase getUserNotificationsUseCase;
    private final MarkNotificationAsReadUseCase markNotificationAsReadUseCase;

    /**
     * Crea una nueva notificación.
     *
     * @param request Datos de la notificación a crear.
     * @return La notificación creada en formato {@link NotificationResponse}.
     */
    @PostMapping
    public ResponseEntity<NotificationResponse> createNotification(
            @Valid @RequestBody CreateNotificationRequest request) {

        InAppNotification domain = NotificationDtoMapper.toDomain(request);
        InAppNotification created = createNotificationUseCase.createNotification(domain);
        NotificationResponse response = NotificationDtoMapper.toResponse(created);

        return ResponseEntity.ok(response);
    }

    /**
     * Obtiene las notificaciones asociadas a un usuario.
     *
     * @param userId Identificador del usuario.
     * @return Lista de notificaciones del usuario en formato {@link NotificationResponse}.
     */
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

    /**
     * Marca una notificación como leída.
     *
     * @param notificationId Identificador de la notificación.
     * @return La notificación actualizada en formato {@link NotificationResponse}.
     */
    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<NotificationResponse> markAsRead(
            @PathVariable UUID notificationId) {

        InAppNotification updated = markNotificationAsReadUseCase.markAsRead(notificationId);
        NotificationResponse response = NotificationDtoMapper.toResponse(updated);

        return ResponseEntity.ok(response);
    }
}
