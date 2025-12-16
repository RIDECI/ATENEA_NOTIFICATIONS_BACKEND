package edu.dosw.rideci.application.events.travel;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Evento que se publica cuando se completa un viaje
 * Routing Key: "travel.completed"
 * Exchange: "travel.exchange"
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TravelCompletedEvent {

    @JsonProperty("travelId")
    private String travelId;

    @JsonProperty("driverId")
    private String driverId;

    @JsonProperty("driverName")
    private String driverName;

    @JsonProperty("passengerIds")
    private List<String> passengerIds;

    @JsonProperty("completedAt")
    private LocalDateTime completedAt;

    @JsonProperty("actualDepartureTime")
    private LocalDateTime actualDepartureTime;

    @JsonProperty("actualArrivalTime")
    private LocalDateTime actualArrivalTime;

    @JsonProperty("distanceTraveled")
    private Double distanceTraveled;

    @JsonProperty("durationMinutes")
    private Integer durationMinutes;

    @JsonProperty("totalAmount")
    private Double totalAmount;

    @JsonProperty("ratingEnabled")
    private Boolean ratingEnabled;

    @JsonProperty("status")
    private String status; // COMPLETED, PARTIALLY_COMPLETED
}