package edu.dosw.rideci.infrastructure.messaging.events.travel;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Evento que se publica cuando se actualiza una ruta existente
 * Routing Key: "travel.updated"
 * Exchange: "travel.exchange"
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TravelUpdatedEvent {

    @JsonProperty("travelId")
    private String travelId;

    @JsonProperty("driverId")
    private String driverId;

    @JsonProperty("origin")
    private TravelCreatedEvent.Location origin;

    @JsonProperty("destination")
    private TravelCreatedEvent.Location destination;

    @JsonProperty("departureTime")
    private LocalDateTime departureTime;

    @JsonProperty("arrivalTime")
    private LocalDateTime arrivalTime;

    @JsonProperty("availableSeats")
    private Integer availableSeats;

    @JsonProperty("pricePerSeat")
    private Double pricePerSeat;

    @JsonProperty("status")
    private String status;

    @JsonProperty("updateReason")
    private String updateReason;

    @JsonProperty("updatedAt")
    private LocalDateTime updatedAt;

    @JsonProperty("changes")
    private String changes; // Descripción de los cambios realizados
}
