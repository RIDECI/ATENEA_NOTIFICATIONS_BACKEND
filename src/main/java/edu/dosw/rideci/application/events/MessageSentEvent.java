package edu.dosw.rideci.application.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageSentEvent {
    private String conversationId;
    private String messageId;
    private String senderId;
    private String receiverId;
    private String content;
    private LocalDateTime sentAt;
}