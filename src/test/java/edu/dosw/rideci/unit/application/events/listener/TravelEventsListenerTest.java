package edu.dosw.rideci.unit.application.events.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.dosw.rideci.application.events.listener.TravelEventsListener;
import edu.dosw.rideci.application.events.travel.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TravelEventsListenerTest {

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private TravelEventsListener travelEventsListener;

    private Message createMessage(String routingKey, String body) {
        MessageProperties properties = new MessageProperties();
        properties.setReceivedRoutingKey(routingKey);
        properties.setReceivedExchange("travel.exchange");
        return new Message(body.getBytes(StandardCharsets.UTF_8), properties);
    }

    @Test
    void handleTravelEvent_ShouldHandleTravelCreated() throws Exception {
        String jsonBody = "{\"travelId\":\"travel-123\"}";
        Message message = createMessage("travel.created", jsonBody);
        TravelCreatedEvent event = new TravelCreatedEvent();
        event.setTravelId("travel-123");

        when(objectMapper.readValue(any(byte[].class), eq(TravelCreatedEvent.class))).thenReturn(event);

        travelEventsListener.handleTravelEvent(message);

        verify(objectMapper, times(1)).readValue(any(byte[].class), eq(TravelCreatedEvent.class));
    }

    @Test
    void handleTravelEvent_ShouldHandleTravelUpdated() throws Exception {
        String jsonBody = "{\"travelId\":\"travel-123\"}";
        Message message = createMessage("travel.updated", jsonBody);
        TravelUpdatedEvent event = new TravelUpdatedEvent();
        event.setTravelId("travel-123");

        when(objectMapper.readValue(any(byte[].class), eq(TravelUpdatedEvent.class))).thenReturn(event);

        travelEventsListener.handleTravelEvent(message);

        verify(objectMapper, times(1)).readValue(any(byte[].class), eq(TravelUpdatedEvent.class));
    }

    @Test
    void handleTravelEvent_ShouldHandleTravelCancelled() throws Exception {
        String jsonBody = "{\"travelId\":\"travel-123\"}";
        Message message = createMessage("travel.cancelled", jsonBody);
        TravelCancelledEvent event = new TravelCancelledEvent();
        event.setTravelId("travel-123");

        when(objectMapper.readValue(any(byte[].class), eq(TravelCancelledEvent.class))).thenReturn(event);

        travelEventsListener.handleTravelEvent(message);

        verify(objectMapper, times(1)).readValue(any(byte[].class), eq(TravelCancelledEvent.class));
    }

    @Test
    void handleTravelEvent_ShouldHandleTravelCompleted() throws Exception {
        String jsonBody = "{\"travelId\":\"travel-123\"}";
        Message message = createMessage("travel.completed", jsonBody);
        TravelCompletedEvent event = new TravelCompletedEvent();
        event.setTravelId("travel-123");

        when(objectMapper.readValue(any(byte[].class), eq(TravelCompletedEvent.class))).thenReturn(event);

        travelEventsListener.handleTravelEvent(message);

        verify(objectMapper, times(1)).readValue(any(byte[].class), eq(TravelCompletedEvent.class));
    }

    @Test
    void handleTravelEvent_ShouldHandleUnknownRoutingKey() {
        Message message = createMessage("travel.unknown", "{}");

        assertDoesNotThrow(() -> travelEventsListener.handleTravelEvent(message));
    }

    @Test
    void handleTravelEvent_ShouldHandleException() throws Exception {
        String jsonBody = "{\"travelId\":\"travel-123\"}";
        Message message = createMessage("travel.created", jsonBody);

        when(objectMapper.readValue(any(byte[].class), eq(TravelCreatedEvent.class)))
                .thenThrow(new RuntimeException("Test exception"));

        assertDoesNotThrow(() -> travelEventsListener.handleTravelEvent(message));
    }

    @Test
    void handleTravelEvent_ShouldHandleTravelCreatedWithFullData() throws Exception {
        TravelCreatedEvent.Location origin = new TravelCreatedEvent.Location();
        origin.setAddress("Origin Address");
        TravelCreatedEvent.Location destination = new TravelCreatedEvent.Location();
        destination.setAddress("Destination Address");

        TravelCreatedEvent event = TravelCreatedEvent.builder()
                .travelId("travel-123")
                .driverId("driver-123")
                .driverName("Driver Name")
                .origin(origin)
                .destination(destination)
                .availableSeats(4)
                .pricePerSeat(50.0)
                .vehicleType("Sedan")
                .vehiclePlate("ABC123")
                .build();

        String jsonBody = "{\"travelId\":\"travel-123\"}";
        Message message = createMessage("travel.created", jsonBody);

        when(objectMapper.readValue(any(byte[].class), eq(TravelCreatedEvent.class))).thenReturn(event);

        travelEventsListener.handleTravelEvent(message);

        verify(objectMapper, times(1)).readValue(any(byte[].class), eq(TravelCreatedEvent.class));
    }

    @Test
    void handleTravelEvent_ShouldHandleTravelCancelledWithFullData() throws Exception {
        TravelCancelledEvent event = TravelCancelledEvent.builder()
                .travelId("travel-123")
                .driverName("Driver Name")
                .cancellationReason("Driver unavailable")
                .cancelledBy("DRIVER")
                .affectedPassengers(3)
                .refundPolicy("Full refund")
                .build();

        String jsonBody = "{\"travelId\":\"travel-123\"}";
        Message message = createMessage("travel.cancelled", jsonBody);

        when(objectMapper.readValue(any(byte[].class), eq(TravelCancelledEvent.class))).thenReturn(event);

        travelEventsListener.handleTravelEvent(message);

        verify(objectMapper, times(1)).readValue(any(byte[].class), eq(TravelCancelledEvent.class));
    }

    @Test
    void handleTravelEvent_ShouldHandleTravelCompletedWithRatingEnabled() throws Exception {
        TravelCompletedEvent event = TravelCompletedEvent.builder()
                .travelId("travel-123")
                .driverId("driver-123")
                .driverName("Driver Name")
                .passengerIds(java.util.Arrays.asList("passenger-1", "passenger-2"))
                .durationMinutes(60)
                .distanceTraveled(50.0)
                .totalAmount(200.0)
                .ratingEnabled(true)
                .build();

        String jsonBody = "{\"travelId\":\"travel-123\"}";
        Message message = createMessage("travel.completed", jsonBody);

        when(objectMapper.readValue(any(byte[].class), eq(TravelCompletedEvent.class))).thenReturn(event);

        travelEventsListener.handleTravelEvent(message);

        verify(objectMapper, times(1)).readValue(any(byte[].class), eq(TravelCompletedEvent.class));
    }
}

