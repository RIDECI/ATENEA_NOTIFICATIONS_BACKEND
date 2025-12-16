package edu.dosw.rideci.application.events.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetEvent {

    @JsonProperty("email")
    private String email;

    @JsonProperty("resetCode")
    private String resetCode;

    @JsonProperty("expiryDate")
    private LocalDateTime expiryDate;

    @JsonProperty("expiryMinutes")
    private int expiryMinutes;
}