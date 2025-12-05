package edu.dosw.rideci.application.service;

import edu.dosw.rideci.application.events.listener.UserSyncFailedEvent;
import edu.dosw.rideci.application.events.TravelCreatedEvent;
import edu.dosw.rideci.application.events.TravelCompletedEvent;
import edu.dosw.rideci.application.port.out.AppNotificationRepositoryPort;
import edu.dosw.rideci.domain.model.AppNotification;
import edu.dosw.rideci.domain.model.Enum.Category;
import edu.dosw.rideci.domain.model.Enum.MessageType;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class ProcessUserEventService {

    private final AppNotificationRepositoryPort appNotificationRepositoryPort;

    public ProcessUserEventService(AppNotificationRepositoryPort appNotificationRepositoryPort) {
        this.appNotificationRepositoryPort = appNotificationRepositoryPort;
    }

    public void processUserSyncFailed(UserSyncFailedEvent event) {
        String userIdentifier = event.getEmail() != null ?
                event.getEmail() :
                (event.getUserId() != null ? "Usuario ID: " + event.getUserId() : "Usuario desconocido");

        AppNotification notification = AppNotification.builder()
                .user(null)
                .messageType(MessageType.SECURITY_ALERT)
                .message("Error en sincronización de usuario: " + userIdentifier + " - " + event.getReason())
                .timestamp(LocalDateTime.now())
                .category(Category.ALERTS)
                .read(false)
                .build();

        appNotificationRepositoryPort.save(notification);
    }

    public void processTravelCreated(TravelCreatedEvent event) {
        String destino = event.getDestinyAddress() != null
                ? event.getDestinyAddress()
                : (event.getDestinyLatitude() != null && event.getDestinyLongitude() != null ?
                "Lat: " + event.getDestinyLatitude() + ", Lon: " + event.getDestinyLongitude()
                : "Destino no especificado");

        String conductor = event.getDriverId() != null ?
                "Conductor ID: " + event.getDriverId() :
                (event.getOrganizerId() != null ? "Organizador ID: " + event.getOrganizerId() : "Sin conductor");

        AppNotification notification = AppNotification.builder()
                .user(null)
                .messageType(MessageType.TRIP_RESERVED)
                .message("Nuevo viaje creado: " + event.getTravelId() +
                        " - " + conductor +
                        " - Destino: " + destino)
                .timestamp(LocalDateTime.now())
                .category(Category.TRIPS)
                .read(false)
                .build();

        appNotificationRepositoryPort.save(notification);
    }

    public void processTravelCompleted(TravelCompletedEvent event) {
        int pasajeros = event.getPassengerList() != null ? event.getPassengerList().size() : 0;

        AppNotification notification = AppNotification.builder()
                .user(null)
                .messageType(MessageType.TRIP_FINISHED)
                .message("Viaje completado: " + event.getTravelId() +
                        " - Conductor: " + event.getDriverId() +
                        " - Pasajeros: " + pasajeros +
                        " - Estado: " + event.getState())
                .timestamp(LocalDateTime.now())
                .category(Category.TRIPS)
                .read(false)
                .build();

        appNotificationRepositoryPort.save(notification);
    }
}