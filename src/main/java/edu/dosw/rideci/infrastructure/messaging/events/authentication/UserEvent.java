package edu.dosw.rideci.infrastructure.messaging.events.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Evento que se publica cuando se crea o actualiza un usuario
 * Routing Keys probables: "auth.user.created", "auth.user.updated"
 * Exchange: "user.exchange"
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEvent {

    @JsonProperty("userId")
    private String userId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("email")
    private String email;

    @JsonProperty("identificationType")
    private String identificationType;

    @JsonProperty("identificationNumber")
    private String identificationNumber;

    @JsonProperty("phoneNumber")
    private String phoneNumber;

    @JsonProperty("address")
    private String address;

    @JsonProperty("role")
    private String role; // STUDENT, TEACHER, ADMIN, etc.

    @JsonProperty("eventType")
    private String eventType; // CREATED, UPDATED, DELETED

    @JsonProperty("eventTimestamp")
    private LocalDateTime eventTimestamp;

    @JsonProperty("isActive")
    private Boolean isActive;

    @JsonProperty("institutionalId")
    private String institutionalId;
}