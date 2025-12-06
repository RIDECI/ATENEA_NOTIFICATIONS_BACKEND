package edu.dosw.rideci.application.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class TravelCreatedEvent {
    private String travelId;
    private Long organizerId;
    private Long driverId;
    private Integer availableSlots;
    private Double estimatedCost;
    private String status;

    private Object origin;
    private Object destiny;

    private List<Long> passengersId;
    private String travelType;
    private LocalDateTime departureDateAndTime;
    private String conditions;


    public String getOriginAddress() {
        return extractAddress(origin);
    }

    public Double getOriginLatitude() {
        return extractLatitude(origin);
    }

    public Double getOriginLongitude() {
        return extractLongitude(origin);
    }

    public String getDestinyAddress() {
        return extractAddress(destiny);
    }

    public Double getDestinyLatitude() {
        return extractLatitude(destiny);
    }

    public Double getDestinyLongitude() {
        return extractLongitude(destiny);
    }

    private String extractAddress(Object location) {
        if (location == null) return null;
        if (location instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) location;
            Object address = map.get("address");
            if (address == null) address = map.get("direction");
            return address != null ? address.toString() : null;
        }
        return location.toString();
    }

    private Double extractLatitude(Object location) {
        if (location == null) return null;
        if (location instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) location;
            Object lat = map.get("latitude");
            return lat != null ? Double.parseDouble(lat.toString()) : null;
        }
        return null;
    }

    private Double extractLongitude(Object location) {
        if (location == null) return null;
        if (location instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) location;
            Object lon = map.get("longitude");
            return lon != null ? Double.parseDouble(lon.toString()) : null;
        }
        return null;
    }
}