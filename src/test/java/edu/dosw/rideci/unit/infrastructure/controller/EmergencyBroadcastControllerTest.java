package edu.dosw.rideci.unit.infrastructure.controller;

import edu.dosw.rideci.application.port.in.CreateEmergencyBroadcastUseCase;
import edu.dosw.rideci.domain.model.InAppNotification;
import edu.dosw.rideci.infrastructure.controller.EmergencyBroadcastController;
import edu.dosw.rideci.infrastructure.controller.dto.Request.EmergencyBroadcastRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmergencyBroadcastControllerTest {

    @Mock
    private CreateEmergencyBroadcastUseCase createEmergencyBroadcastUseCase;

    private EmergencyBroadcastController controller;

    @BeforeEach
    void setUp() {
        controller = new EmergencyBroadcastController(createEmergencyBroadcastUseCase);
    }

    @Test
    void createEmergencyBroadcast_ShouldReturnOkWhenValid() {
        EmergencyBroadcastRequest request = new EmergencyBroadcastRequest(
                "Emergency message",
                "EMERGENCY",
                null,
                "HIGH"
        );

        List<InAppNotification> notifications = Arrays.asList(
                new InAppNotification(),
                new InAppNotification()
        );

        when(createEmergencyBroadcastUseCase.createEmergencyBroadcast(any())).thenReturn(notifications);

        ResponseEntity<?> response = controller.createEmergencyBroadcast(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().toString().contains("2"));
        verify(createEmergencyBroadcastUseCase).createEmergencyBroadcast(any());
    }

    @Test
    void createEmergencyBroadcast_ShouldReturnBadRequestWhenMessageIsNull() {
        EmergencyBroadcastRequest request = new EmergencyBroadcastRequest(
                null,
                "EMERGENCY",
                null,
                "HIGH"
        );

        ResponseEntity<?> response = controller.createEmergencyBroadcast(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("emergencyMessage es obligatorio", response.getBody());
        verify(createEmergencyBroadcastUseCase, never()).createEmergencyBroadcast(any());
    }

    @Test
    void createEmergencyBroadcast_ShouldReturnBadRequestWhenMessageIsBlank() {
        EmergencyBroadcastRequest request = new EmergencyBroadcastRequest(
                "   ",
                "EMERGENCY",
                null,
                "HIGH"
        );

        ResponseEntity<?> response = controller.createEmergencyBroadcast(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("emergencyMessage es obligatorio", response.getBody());
        verify(createEmergencyBroadcastUseCase, never()).createEmergencyBroadcast(any());
    }

    @Test
    void createEmergencyBroadcast_ShouldReturnBadRequestWhenBroadcastTypeIsInvalid() {
        EmergencyBroadcastRequest request = new EmergencyBroadcastRequest(
                "Emergency message",
                "INVALID_TYPE",
                null,
                "HIGH"
        );

        ResponseEntity<?> response = controller.createEmergencyBroadcast(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("broadcastType inválido"));
        verify(createEmergencyBroadcastUseCase, never()).createEmergencyBroadcast(any());
    }

    @Test
    void createEmergencyBroadcast_ShouldHandleEmergencyBroadcastType() {
        EmergencyBroadcastRequest request = new EmergencyBroadcastRequest(
                "Emergency message",
                "EMERGENCY",
                null,
                "HIGH"
        );

        List<InAppNotification> notifications = Arrays.asList(new InAppNotification());

        when(createEmergencyBroadcastUseCase.createEmergencyBroadcast(any())).thenReturn(notifications);

        ResponseEntity<?> response = controller.createEmergencyBroadcast(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(createEmergencyBroadcastUseCase).createEmergencyBroadcast(any());
    }

    @Test
    void createEmergencyBroadcast_ShouldHandleSystemAnnouncementBroadcastType() {
        EmergencyBroadcastRequest request = new EmergencyBroadcastRequest(
                "Emergency message",
                "SYSTEM_ANNOUNCEMENT",
                Arrays.asList("user1", "user2"),
                "HIGH"
        );

        List<InAppNotification> notifications = Arrays.asList(new InAppNotification());

        when(createEmergencyBroadcastUseCase.createEmergencyBroadcast(any())).thenReturn(notifications);

        ResponseEntity<?> response = controller.createEmergencyBroadcast(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(createEmergencyBroadcastUseCase).createEmergencyBroadcast(any());
    }

    @Test
    void createEmergencyBroadcast_ShouldHandleException() {
        EmergencyBroadcastRequest request = new EmergencyBroadcastRequest(
                "Emergency message",
                "EMERGENCY",
                null,
                "HIGH"
        );

        when(createEmergencyBroadcastUseCase.createEmergencyBroadcast(any()))
                .thenThrow(new RuntimeException("Test exception"));

        ResponseEntity<?> response = controller.createEmergencyBroadcast(request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error interno al crear broadcast de emergencia", response.getBody());
    }

    @Test
    void createEmergencyBroadcast_ShouldReturnBadRequestWhenBroadcastTypeIsNull() {
        EmergencyBroadcastRequest request = new EmergencyBroadcastRequest(
                "Emergency message",
                null,
                null,
                "HIGH"
        );

        ResponseEntity<?> response = controller.createEmergencyBroadcast(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("broadcastType es obligatorio"));
        verify(createEmergencyBroadcastUseCase, never()).createEmergencyBroadcast(any());
    }

    @Test
    void createEmergencyBroadcast_ShouldReturnBadRequestWhenBroadcastTypeIsBlank() {
        EmergencyBroadcastRequest request = new EmergencyBroadcastRequest(
                "Emergency message",
                "   ",
                null,
                "HIGH"
        );

        ResponseEntity<?> response = controller.createEmergencyBroadcast(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("broadcastType es obligatorio"));
        verify(createEmergencyBroadcastUseCase, never()).createEmergencyBroadcast(any());
    }
}

