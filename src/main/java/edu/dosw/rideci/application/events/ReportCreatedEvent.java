package edu.dosw.rideci.application.events;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class ReportCreatedEvent {
    private String reportId;
    private Long userId;
    private Long targetId;
    private Long tripId;
    private String type;

    // En lugar de Location, usar campos separados
    private Double locationLatitude;
    private Double locationLongitude;
    private String locationAddress;

    private String description;
    private LocalDateTime createdAt;
}