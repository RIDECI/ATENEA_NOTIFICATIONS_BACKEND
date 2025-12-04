package edu.dosw.rideci.application.events.listener;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserSyncFailedEvent {
    private String email;
    private String reason;
    private LocalDateTime timestamp;

    public UserSyncFailedEvent() {
        this.timestamp = LocalDateTime.now();
    }
}