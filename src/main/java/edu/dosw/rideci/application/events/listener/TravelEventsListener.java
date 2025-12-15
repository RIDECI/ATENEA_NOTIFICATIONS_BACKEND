package edu.dosw.rideci.application.events.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.dosw.rideci.application.events.travel.TravelCancelledEvent;
import edu.dosw.rideci.application.events.travel.TravelCompletedEvent;
import edu.dosw.rideci.application.events.travel.TravelCreatedEvent;
import edu.dosw.rideci.application.events.travel.TravelUpdatedEvent;
import edu.dosw.rideci.infrastructure.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TravelEventsListener {

    private final ObjectMapper objectMapper;

    @RabbitListener(queues = RabbitMQConfig.NOTIF_TRAVEL_EVENTS_QUEUE)
    public void handleTravelEvent(Message message) {
        try {
            String routingKey = message.getMessageProperties().getReceivedRoutingKey();
            String exchange = message.getMessageProperties().getReceivedExchange();

            log.info("🗺️  Evento de Viaje recibido desde {}: {}", exchange, routingKey);

            switch (routingKey) {
                case "travel.created":
                    handleTravelCreated(message);
                    break;

                case "travel.updated":
                    handleTravelUpdated(message);
                    break;

                case "travel.cancelled":
                    handleTravelCancelled(message);
                    break;

                case "travel.completed":
                    handleTravelCompleted(message);
                    break;

                default:
                    log.debug("Routing key no manejado en viajes: {}", routingKey);
            }

        } catch (Exception e) {
            log.error("❌ Error procesando evento de viaje", e);
        }
    }

    private void handleTravelCreated(Message message) throws Exception {
        TravelCreatedEvent event = objectMapper.readValue(message.getBody(), TravelCreatedEvent.class);

        log.info("🚗 Nuevo viaje creado - ID: {}", event.getTravelId());
        log.info("   Conductor: {} (ID: {})", event.getDriverName(), event.getDriverId());
        log.info("   Ruta: {} → {}",
                event.getOrigin().getAddress(),
                event.getDestination().getAddress());
        log.info("   Salida: {}", event.getDepartureTime());
        log.info("   Asientos: {} | Precio: ${}",
                event.getAvailableSeats(), event.getPricePerSeat());
        log.info("   Vehículo: {} - {}", event.getVehicleType(), event.getVehiclePlate());

        log.info("   📢 Notificando a usuarios cercanos sobre nuevo viaje disponible");
    }

    private void handleTravelUpdated(Message message) throws Exception {
        TravelUpdatedEvent event = objectMapper.readValue(message.getBody(), TravelUpdatedEvent.class);

        log.info("✏️  Viaje actualizado - ID: {}", event.getTravelId());
        log.info("   Razón: {}", event.getUpdateReason());
        log.info("   Cambios: {}", event.getChanges());
        log.info("   Nuevos datos - Salida: {}, Asientos: {}, Precio: ${}",
                event.getDepartureTime(), event.getAvailableSeats(), event.getPricePerSeat());

        log.info("   📢 Notificando a pasajeros sobre cambios en el viaje");
    }

    private void handleTravelCancelled(Message message) throws Exception {
        TravelCancelledEvent event = objectMapper.readValue(message.getBody(), TravelCancelledEvent.class);

        log.info("❌ Viaje cancelado - ID: {}", event.getTravelId());
        log.info("   Conductor: {}", event.getDriverName());
        log.info("   Razón: {}", event.getCancellationReason());
        log.info("   Cancelado por: {}", event.getCancelledBy());
        log.info("   Pasajeros afectados: {}", event.getAffectedPassengers());
        log.info("   Política de reembolso: {}", event.getRefundPolicy());

        log.info("   📢 Notificando a {} pasajeros sobre cancelación", event.getAffectedPassengers());
    }

    private void handleTravelCompleted(Message message) throws Exception {
        TravelCompletedEvent event = objectMapper.readValue(message.getBody(), TravelCompletedEvent.class);

        log.info("✅ Viaje completado - ID: {}", event.getTravelId());
        log.info("   Conductor: {} (ID: {})", event.getDriverName(), event.getDriverId());
        log.info("   Pasajeros: {}", event.getPassengerIds().size());
        log.info("   Duración: {} minutos", event.getDurationMinutes());
        log.info("   Distancia: {} km", event.getDistanceTraveled());
        log.info("   Total recaudado: ${}", event.getTotalAmount());
        log.info("   Habilitar calificación: {}", event.getRatingEnabled());

        if (Boolean.TRUE.equals(event.getRatingEnabled())) {
            log.info("   ⭐ Enviando solicitud de calificación a conductor y pasajeros");
        }
    }
}
