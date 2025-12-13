package edu.dosw.rideci.application.events.booking;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Evento que se publica cuando un pasajero crea una reserva en un viaje
 * Routing Key: "booking.created"
 * Exchange: "booking.exchange"
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingCreatedEvent {

    @JsonProperty("bookingId")
    private String bookingId;

    @JsonProperty("travelId")
    private String travelId;

    @JsonProperty("origin")
    private String origin;

    @JsonProperty("destination")
    private String destination;

    @JsonProperty("passengerId")
    private Long passengerId;

    @JsonProperty("reservedSeats")
    private int reservedSeats;

    @JsonProperty("timestamp")
    private LocalDateTime timestamp;
}