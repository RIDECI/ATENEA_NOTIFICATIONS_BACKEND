package edu.dosw.rideci.application.events.command;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class EmergencyAlertEvent {
    private String eventId;
    private LocalDateTime timestamp;
    private String userId;
    private String emergencyType;
    private String location;
    private String message;
    private String priorityLevel;

    public EmergencyAlertEvent() {
        this.timestamp = LocalDateTime.now();
        this.eventId = java.util.UUID.randomUUID().toString();
    }
}