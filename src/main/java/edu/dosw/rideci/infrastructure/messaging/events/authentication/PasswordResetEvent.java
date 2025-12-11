package edu.dosw.rideci.infrastructure.messaging.events.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Evento que se publica cuando un usuario solicita restablecer su contraseña
 * Routing Keys probables: "auth.user.password.reset"
 * Exchange: "user.exchange"
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordResetEvent {

    @JsonProperty("userId")
    private String userId;

    @JsonProperty("email")
    private String email;

    @JsonProperty("userName")
    private String userName;

    @JsonProperty("resetToken")
    private String resetToken;

    @JsonProperty("resetCode")
    private String resetCode;

    @JsonProperty("expiryDate")
    private LocalDateTime expiryDate;

    @JsonProperty("expiryMinutes")
    private Integer expiryMinutes;

    @JsonProperty("requestedAt")
    private LocalDateTime requestedAt;
}