package edu.dosw.rideci.infrastructure.messaging.events.communication_security;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Evento que se publica cuando un usuario crea un reporte de seguridad
 * Routing Key: "report.created"
 * Exchange: "rideci.report.exchange"
 *
 * Tu servicio escuchará este evento para:
 * 1. Notificar al usuario que su reporte fue recibido
 * 2. Alertar a administradores sobre reportes críticos
 * 3. Notificar al equipo de seguridad en emergencias
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportCreatedEvent {

    @JsonProperty("reportId")
    private String reportId;

    @JsonProperty("userId")
    private String userId;

    @JsonProperty("userName")
    private String userName;

    @JsonProperty("userEmail")
    private String userEmail;

    @JsonProperty("reportedUserId")
    private String reportedUserId; // Usuario reportado (opcional)

    @JsonProperty("reportedUserName")
    private String reportedUserName;

    @JsonProperty("travelId")
    private Long travelId; // Viaje relacionado (opcional)

    @JsonProperty("reportType")
    private String reportType; // SAFETY, BEHAVIOR, EMERGENCY, TECHNICAL, OTHER

    @JsonProperty("subtype")
    private String subtype; // HARASSMENT, THEFT, ACCIDENT, BAD_DRIVING, etc.

    @JsonProperty("severity")
    private String severity; // LOW, MEDIUM, HIGH, CRITICAL

    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;

    @JsonProperty("latitude")
    private Double latitude;

    @JsonProperty("longitude")
    private Double longitude;

    @JsonProperty("address")
    private String address;

    @JsonProperty("createdAt")
    private LocalDateTime createdAt;

    @JsonProperty("requiresImmediateAction")
    private Boolean requiresImmediateAction;

    @JsonProperty("status")
    private String status; // PENDING, IN_REVIEW, RESOLVED, DISMISSED

    @JsonProperty("priority")
    private String priority; // NORMAL, URGENT, EMERGENCY
}