package edu.dosw.rideci.application.events.listener;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserCreatedEvent {
    private String eventType = "USER_CREATED";
    private LocalDateTime timestamp;
    private Long userId;
    private String name;
    private String email;
    private String phoneNumber;
    private String role;

    public UserCreatedEvent() {
        this.timestamp = LocalDateTime.now();
    }
}