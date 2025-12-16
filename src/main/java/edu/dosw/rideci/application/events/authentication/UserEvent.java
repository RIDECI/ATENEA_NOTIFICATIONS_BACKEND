package edu.dosw.rideci.application.events.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para enviar a UserManagement via RabbitMQ
 * Mensaje para crear un nuevo usuario
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEvent {

    @JsonProperty("userId")
    private Long userId;

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
    private String role;
}