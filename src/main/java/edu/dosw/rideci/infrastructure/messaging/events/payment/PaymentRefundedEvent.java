package edu.dosw.rideci.infrastructure.messaging.events.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Evento que representa un reembolso procesado
 * Routing Key esperada: "refund.completed"
 * Exchange: "payment.exchange"
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRefundedEvent {

    @JsonProperty("refundId")
    private String refundId;

    @JsonProperty("originalPaymentId")
    private String originalPaymentId;

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