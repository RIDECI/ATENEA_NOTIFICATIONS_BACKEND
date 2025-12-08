package edu.dosw.rideci.infrastructure.messaging.events.travel;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Evento que se publica cuando se crea un nuevo viaje/ruta
 * Routing Key: "travel.created"
 * Exchange: "travel.exchange"
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TravelCreatedEvent {

    @JsonProperty("travelId")
    private String travelId;

    @JsonProperty("driverId")
    private String driverId;

    @JsonProperty("driverName")
    private String driverName;

    @JsonProperty("origin")
    private Location origin;

    @JsonProperty("destination")
    private Location destination;

    @JsonProperty("departureTime")
    private LocalDateTime departureTime;

    @JsonProperty("arrivalTime")
    private LocalDateTime arrivalTime;

    @JsonProperty("availableSeats")
    private Integer availableSeats;

    @JsonProperty("pricePerSeat")
    private Double pricePerSeat;

    @JsonProperty("vehicleType")
    private String vehicleType;

    @JsonProperty("vehiclePlate")
    private String vehiclePlate;

    @JsonProperty("status")
    private String status;

    @JsonProperty("createdAt")
    private LocalDateTime createdAt;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Location {
        @JsonProperty("latitude")
        private Double latitude;

        @JsonProperty("longitude")
        private Double longitude;

        @JsonProperty("address")
        private String address;

        @JsonProperty("city")
        private String city;
    }
}
