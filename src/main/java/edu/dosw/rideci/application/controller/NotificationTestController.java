package edu.dosw.rideci.application.controller;

import edu.dosw.rideci.application.port.in.CreateNotificationUseCase;
import edu.dosw.rideci.domain.model.InAppNotification;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class NotificationTestController {

    private final CreateNotificationUseCase createNotificationUseCase;

    @PostMapping("/notification")
    public InAppNotification createTestNotification() {
        InAppNotification notification = InAppNotification.builder()
                .userId(UUID.randomUUID())
                .title("Prueba correo RIDECI")
                .message("Esta es una notificación de prueba para verificar el envío de email.")
                .build();

        return createNotificationUseCase.createNotification(notification);
    }
}