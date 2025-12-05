package edu.dosw.rideci.application.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TravelCreatedEvent {
    private String travelId;
    private Long organizerId;
    private Long driverId;
    private Integer availableSlots;
    private Double estimatedCost;
    private String status;

    // En lugar de Location, usar Strings separados
    private String originAddress;
    private Double originLatitude;
    private Double originLongitude;

    private String destinyAddress;
    private Double destinyLatitude;
    private Double destinyLongitude;

    private List<Long> passengersId;
    private String travelType;
    private LocalDateTime departureDateAndTime;
    private String conditions;
}