package edu.dosw.rideci.infrastructure.messaging.events.communication_security;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Evento que se publica cuando un usuario envía un mensaje en el chat
 * Routing Key: "chat.message"
 * Exchange: "rideci.chat.exchange"
 *
 * Tu servicio escuchará este evento para notificar a los usuarios sobre nuevos mensajes
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageSentEvent {

    @JsonProperty("conversationId")
    private String conversationId;

    @JsonProperty("messageId")
    private String messageId;

    @JsonProperty("senderId")
    private String senderId;

    @JsonProperty("senderName")
    private String senderName;

    @JsonProperty("receiverId")
    private String receiverId; // Puede ser null si es grupo

    @JsonProperty("receiverName")
    private String receiverName;

    @JsonProperty("content")
    private String content;

    @JsonProperty("messageType")
    private String messageType; // TEXT, IMAGE, LOCATION, EMERGENCY

    @JsonProperty("isEmergency")
    private Boolean isEmergency;

    @JsonProperty("travelId")
    private Long travelId; // Opcional, si el mensaje está asociado a un viaje

    @JsonProperty("sentAt")
    private LocalDateTime sentAt;

    @JsonProperty("requiresNotification")
    private Boolean requiresNotification;
}