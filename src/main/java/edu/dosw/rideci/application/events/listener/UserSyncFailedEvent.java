package edu.dosw.rideci.application.events.listener;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserSyncFailedEvent {
    private String email;       // Mantener para compatibilidad
    private String reason;
    private LocalDateTime timestamp;
    private Long userId;        // Agregar para recibir del user management

    public UserSyncFailedEvent() {
        this.timestamp = LocalDateTime.now();
    }
}