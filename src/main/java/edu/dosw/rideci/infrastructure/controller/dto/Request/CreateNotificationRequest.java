package edu.dosw.rideci.infrastructure.controller.dto.Request;

import edu.dosw.rideci.domain.model.Enum.EventType;
import edu.dosw.rideci.domain.model.Enum.NotificationChannel;
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
