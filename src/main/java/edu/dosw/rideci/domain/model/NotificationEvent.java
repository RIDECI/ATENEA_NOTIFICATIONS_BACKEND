package edu.dosw.rideci.domain.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.dosw.rideci.domain.model.Enum.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationEvent {

    private String eventId;
    private EventType eventType;
    private String sourceModule;
    private String userId;
    private String message;
    private int priority;
    private Instant timestamp;
    private String payload;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public String toJSON() {
        try {
            return MAPPER.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "{\"eventId\":\"" + eventId + "\",\"eventType\":\"" +
                    (eventType != null ? eventType.name() : "UNKNOWN") + "\"}";
        }
    }

}
