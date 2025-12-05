package edu.dosw.rideci.application.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConversationCreatedEvent {
    private String conversationId;
    private String travelId;
    private List<Long> participants;
    private String type;
    private LocalDateTime createdAt;
}