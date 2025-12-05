package edu.dosw.rideci.application.service;

import edu.dosw.rideci.application.events.ReportCreatedEvent;
import edu.dosw.rideci.application.events.ConversationCreatedEvent;
import edu.dosw.rideci.application.events.MessageSentEvent;
import edu.dosw.rideci.application.port.out.AppNotificationRepositoryPort;
import edu.dosw.rideci.domain.model.AppNotification;
import edu.dosw.rideci.domain.model.Enum.Category;
import edu.dosw.rideci.domain.model.Enum.MessageType;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class SecurityEventService {

    private final AppNotificationRepositoryPort appNotificationRepositoryPort;

    public SecurityEventService(AppNotificationRepositoryPort appNotificationRepositoryPort) {
        this.appNotificationRepositoryPort = appNotificationRepositoryPort;
    }

    public void processReportCreated(ReportCreatedEvent event) {
        String ubicacion = event.getLocationAddress() != null
                ? event.getLocationAddress()
                : (event.getLocationLatitude() != null && event.getLocationLongitude() != null ?
                "Lat: " + event.getLocationLatitude() + ", Lon: " + event.getLocationLongitude()
                : "Ubicación no especificada");

        String descripcionResumen = event.getDescription() != null && event.getDescription().length() > 50 ?
                event.getDescription().substring(0, 50) + "..." : event.getDescription();

        AppNotification notification = AppNotification.builder()
                .user(null)
                .messageType(MessageType.SECURITY_ALERT)
                .message("Nuevo reporte creado: " + event.getType() +
                        " - ID Reporte: " + event.getReportId() +
                        " - Usuario: " + event.getUserId() +
                        " - " + descripcionResumen)
                .timestamp(LocalDateTime.now())
                .category(Category.ALERTS)
                .read(false)
                .build();

        appNotificationRepositoryPort.save(notification);
    }

    public void processConversationCreated(ConversationCreatedEvent event) {
        int participantes = event.getParticipants() != null ? event.getParticipants().size() : 0;
        String tipoConversacion = event.getType() != null ? event.getType() : "General";

        AppNotification notification = AppNotification.builder()
                .user(null)
                .messageType(MessageType.SECURITY_ALERT)
                .message("Nueva conversación creada: " + event.getConversationId() +
                        " - Tipo: " + tipoConversacion +
                        " - Viaje: " + event.getTravelId() +
                        " - Participantes: " + participantes)
                .timestamp(LocalDateTime.now())
                .category(Category.IMPORTANT)
                .read(false)
                .build();

        appNotificationRepositoryPort.save(notification);
    }

    public void processMessageSent(MessageSentEvent event) {
        String contenidoResumen = event.getContent() != null && event.getContent().length() > 40 ?
                event.getContent().substring(0, 40) + "..." : event.getContent();

        AppNotification notification = AppNotification.builder()
                .user(null)
                .messageType(MessageType.SECURITY_ALERT)
                .message("Nuevo mensaje: Conversación " + event.getConversationId() +
                        " - De: " + event.getSenderId() +
                        " a: " + event.getReceiverId() +
                        " - " + contenidoResumen)
                .timestamp(LocalDateTime.now())
                .category(Category.ALERTS)
                .read(false)
                .build();

        appNotificationRepositoryPort.save(notification);
    }
}