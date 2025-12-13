package edu.dosw.rideci.application.events.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Evento que representa un pago que ha fallado
 * Routing Key esperada: "payment.failed"
 * Exchange: "payment.exchange"
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentFailedEvent {

    @JsonProperty("paymentId")
    private String paymentId;

    @JsonProperty("userId")
    private String userId;

    @JsonProperty("driverId")
    private String driverId;

    @JsonProperty("tripId")
    private String tripId;

    @JsonProperty("amount")
    private Double amount;

    @JsonProperty("timestamp")
    private String timestamp;
}