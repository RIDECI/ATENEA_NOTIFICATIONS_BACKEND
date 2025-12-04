package edu.dosw.rideci.infrastructure.controller.dto.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CreateNotificationRequest {

    @NotNull
    private UUID userId;

    @NotNull
    private EventType eventType;

    @NotNull
    private NotificationChannel channel;

    @NotBlank
    private String title;

    @NotBlank
    private String message;

    private String priority;
    private String metadataJson;
}
