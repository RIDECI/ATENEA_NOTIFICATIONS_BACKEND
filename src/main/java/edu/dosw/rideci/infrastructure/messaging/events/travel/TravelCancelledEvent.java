package edu.dosw.rideci.infrastructure.messaging.events.travel;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Evento que se publica cuando se cancela un viaje/ruta
 * Routing Key: "travel.cancelled"
 * Exchange: "travel.exchange"
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TravelCancelledEvent {

    @JsonProperty("travelId")
    private String travelId;

    @JsonProperty("driverId")
    private String driverId;

    @JsonProperty("driverName")
    private String driverName;

    @JsonProperty("cancellationReason")
    private String cancellationReason;

    @JsonProperty("cancelledBy")
    private String cancelledBy; // DRIVER, SYSTEM, ADMIN

    @JsonProperty("cancelledAt")
    private LocalDateTime cancelledAt;

    @JsonProperty("refundPolicy")
    private String refundPolicy;

    @JsonProperty("affectedPassengers")
    private Integer affectedPassengers;

    @JsonProperty("originalDepartureTime")
    private LocalDateTime originalDepartureTime;
}