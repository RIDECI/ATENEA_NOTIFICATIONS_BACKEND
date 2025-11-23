package edu.dosw.rideci.ATENEA_NOTIFICATIONS_BACKEND.domain.event;
import java.time.LocalDateTime;
import java.util.Map;


public abstract class NotificationsEvent {
    protected String eventId;
    protected String eventType;
    protected String sourceModule;
    protected LocalDateTime timestamp;
    protected Map<String, Object> payload;
    protected String priority;
    protected String userId;

 
    public NotificationsEvent(String eventId, String eventType, String sourceModule, LocalDateTime timestamp, Map<String, Object> payload, String priority, String userId) {
        this.eventId = eventId;
        this.eventType = eventType;
        this.sourceModule = sourceModule;
        this.timestamp = timestamp;
        this.payload = payload;
        this.priority = priority;
        this.userId = userId;
    }
    public String getEventId() {
        return eventId;
    }


    public boolean validate() {
        if (eventType == null || eventType.isBlank()) return false;
        if (userId == null || userId.isBlank()) return false;
        if (timestamp == null) return false;
        if (payload == null) return false;
        return true;
    }

    public String toJSON() {
        return "{"
                + "\"eventId\":\"" + eventId + "\","
                + "\"eventType\":\"" + eventType + "\","
                + "\"sourceModule\":\"" + sourceModule + "\","
                + "\"timestamp\":\"" + timestamp + "\","
                + "\"priority\":\"" + priority + "\","
                + "\"userId\":\"" + userId + "\""
                + "}";
    }
}