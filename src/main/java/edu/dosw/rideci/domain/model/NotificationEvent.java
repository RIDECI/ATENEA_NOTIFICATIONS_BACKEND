package edu.dosw.rideci.domain.model;

import edu.dosw.rideci.domain.model.Enum.EventType;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

@Getter
public abstract class NotificationEvent {

    @Getter
    private final String eventId;
    private final EventType eventType;
    private final String sourceModule;
    private final OffsetDateTime timestamp;
    private final Map<String, Object> payload;
    private final String priority; // "LOW", "NORMAL", "HIGH"
    private final UUID userId;

    protected NotificationEvent(EventType eventType,
                                String sourceModule,
                                Map<String, Object> payload,
                                String priority,
                                UUID userId) {

        this.eventId = UUID.randomUUID().toString();
        this.eventType = eventType;
        this.sourceModule = sourceModule;
        this.timestamp = OffsetDateTime.now();
        this.payload = payload;
        this.priority = priority;
        this.userId = userId;
    }

    public abstract boolean validate();

    public String toJSON() {
        return "NotificationEvent{eventId=" + eventId + ", eventType=" + eventType + "}";
    }

}
