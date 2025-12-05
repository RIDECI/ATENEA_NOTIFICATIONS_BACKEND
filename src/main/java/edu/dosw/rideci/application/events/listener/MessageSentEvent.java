package edu.dosw.rideci.application.events.listener;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageSentEvent {
    private String conversationId;
    private String senderId;
    private String receiverId;
    private String content;
}