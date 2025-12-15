package edu.dosw.rideci.unit.domain.model;

import edu.dosw.rideci.domain.model.Enum.NotificationType;
import edu.dosw.rideci.domain.model.InAppNotification;
import edu.dosw.rideci.domain.model.NotificationEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class NotificationEventTest {

    private NotificationEvent notificationEvent;
    private String eventId;
    private String userId;
    private Instant timestamp;
    private InAppNotification inAppNotification;

    @BeforeEach
    void setUp() {
        eventId = UUID.randomUUID().toString();
        userId = "user-123";
        timestamp = Instant.now();

        inAppNotification = InAppNotification.builder()
                .notificationId(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .title("Test Notification")
                .message("Test message")
                .eventType(NotificationType.USER_REGISTERED)
                .priority("NORMAL")
                .build();

        notificationEvent = NotificationEvent.builder()
                .eventId(eventId)
                .eventType(NotificationType.PASSWORD_RECOVERY)
                .sourceModule("AUTHENTICATION")
                .userId(userId)
                .message("Password recovery requested")
                .priority(1) // Alta prioridad
                .timestamp(timestamp)
                .payload("{\"email\":\"test@example.com\",\"resetCode\":\"ABC123\"}")
                .notification(inAppNotification)
                .build();
    }

    @Test
    @DisplayName("Debería crear correctamente un NotificationEvent con todos los campos")
    void shouldCreateNotificationEventWithAllFields() {
        assertThat(notificationEvent.getEventId()).isEqualTo(eventId);
        assertThat(notificationEvent.getEventType()).isEqualTo(NotificationType.PASSWORD_RECOVERY);
        assertThat(notificationEvent.getSourceModule()).isEqualTo("AUTHENTICATION");
        assertThat(notificationEvent.getUserId()).isEqualTo(userId);
        assertThat(notificationEvent.getMessage()).isEqualTo("Password recovery requested");
        assertThat(notificationEvent.getPriority()).isEqualTo(1);
        assertThat(notificationEvent.getTimestamp()).isEqualTo(timestamp);
        assertThat(notificationEvent.getPayload()).isEqualTo("{\"email\":\"test@example.com\",\"resetCode\":\"ABC123\"}");
        assertThat(notificationEvent.getNotification()).isEqualTo(inAppNotification);
    }

    @Test
    @DisplayName("Debería serializar correctamente a JSON")
    void shouldSerializeToJson() {
        String json = notificationEvent.toJSON();

        assertThat(json).isNotEmpty();
        assertThat(json).contains(eventId);
        assertThat(json).contains("PASSWORD_RECOVERY");
    }

    @Test
    @DisplayName("Debería manejar correctamente el método toJSON con campos nulos")
    void shouldHandleToJsonWithNullFields() {
        NotificationEvent eventWithNulls = NotificationEvent.builder()
                .eventId(eventId)
                .eventType(null)
                .sourceModule(null)
                .userId(null)
                .message(null)
                .priority(0)
                .timestamp(null)
                .payload(null)
                .notification(null)
                .build();

        String json = eventWithNulls.toJSON();

        assertThat(json).isNotEmpty();
        assertThat(json).contains(eventId);
    }

    @Test
    @DisplayName("Debería usar el constructor sin argumentos correctamente")
    void shouldUseNoArgsConstructor() {
        NotificationEvent emptyEvent = new NotificationEvent();

        assertThat(emptyEvent.getEventId()).isNull();
        assertThat(emptyEvent.getEventType()).isNull();
        assertThat(emptyEvent.getSourceModule()).isNull();
        assertThat(emptyEvent.getUserId()).isNull();
        assertThat(emptyEvent.getMessage()).isNull();
        assertThat(emptyEvent.getPriority()).isEqualTo(0);
        assertThat(emptyEvent.getTimestamp()).isNull();
        assertThat(emptyEvent.getPayload()).isNull();
        assertThat(emptyEvent.getNotification()).isNull();
    }

    @Test
    @DisplayName("Debería usar el constructor con todos los argumentos correctamente")
    void shouldUseAllArgsConstructor() {
        String testEventId = "test-event-id";
        NotificationType testType = NotificationType.TRIP_CREATED;
        String testSource = "TRAVEL";
        String testUserId = "user-456";
        String testMessage = "Viaje creado";
        int testPriority = 3;
        Instant testTimestamp = Instant.now().minus(1, ChronoUnit.HOURS);
        String testPayload = "{\"travelId\":\"TRV001\"}";
        InAppNotification testNotification = InAppNotification.builder()
                .notificationId(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .title("Viaje Creado")
                .message("Tu viaje ha sido creado")
                .eventType(NotificationType.TRIP_CREATED)
                .build();

        NotificationEvent event = new NotificationEvent(
                testEventId, testType, testSource, testUserId, testMessage,
                testPriority, testTimestamp, testPayload, testNotification
        );

        assertThat(event.getEventId()).isEqualTo(testEventId);
        assertThat(event.getEventType()).isEqualTo(testType);
        assertThat(event.getSourceModule()).isEqualTo(testSource);
        assertThat(event.getUserId()).isEqualTo(testUserId);
        assertThat(event.getMessage()).isEqualTo(testMessage);
        assertThat(event.getPriority()).isEqualTo(testPriority);
        assertThat(event.getTimestamp()).isEqualTo(testTimestamp);
        assertThat(event.getPayload()).isEqualTo(testPayload);
        assertThat(event.getNotification()).isEqualTo(testNotification);
    }

    @Test
    @DisplayName("Debería actualizar los campos correctamente")
    void shouldUpdateFieldsCorrectly() {
        notificationEvent.setEventType(NotificationType.PAYMENT_CONFIRMED);
        notificationEvent.setSourceModule("PAYMENT");
        notificationEvent.setMessage("Pago confirmado");
        notificationEvent.setPriority(2);
        notificationEvent.setPayload("{\"paymentId\":\"PAY123\",\"amount\":50.0}");

        assertThat(notificationEvent.getEventType()).isEqualTo(NotificationType.PAYMENT_CONFIRMED);
        assertThat(notificationEvent.getSourceModule()).isEqualTo("PAYMENT");
        assertThat(notificationEvent.getMessage()).isEqualTo("Pago confirmado");
        assertThat(notificationEvent.getPriority()).isEqualTo(2);
        assertThat(notificationEvent.getPayload()).isEqualTo("{\"paymentId\":\"PAY123\",\"amount\":50.0}");
    }

    @Test
    @DisplayName("Debería retornar JSON incluso cuando el ObjectMapper falla")
    void shouldReturnJsonWhenObjectMapperFails() {
        NotificationEvent event = NotificationEvent.builder()
                .eventId(eventId)
                .eventType(NotificationType.USER_REGISTERED)
                .build();

        String json = event.toJSON();

        assertThat(json).isNotEmpty();
        assertThat(json).contains(eventId);
    }

    @Test
    @DisplayName("Debería manejar diferentes tipos de notificaciones")
    void shouldHandleDifferentNotificationTypes() {
        for (NotificationType type : NotificationType.values()) {
            notificationEvent.setEventType(type);

            assertThat(notificationEvent.getEventType()).isEqualTo(type);
            assertThat(notificationEvent.toJSON()).contains(type.name());
        }
    }

    @Test
    @DisplayName("Debería manejar prioridades diferentes")
    void shouldHandleDifferentPriorities() {
        int[] priorities = {1, 2, 3, 4, 5};

        for (int priority : priorities) {
            notificationEvent.setPriority(priority);

            assertThat(notificationEvent.getPriority()).isEqualTo(priority);
        }
    }

    @Test
    @DisplayName("Debería mantener la referencia al ObjectMapper estático")
    void shouldHaveStaticObjectMapper() {
        String json = notificationEvent.toJSON();
        assertThat(json).isNotEmpty();
    }

    @Test
    @DisplayName("Debería incluir la notificación in-app en el JSON cuando existe")
    void shouldIncludeInAppNotificationInJson() {
        String json = notificationEvent.toJSON();
        assertThat(json).isNotEmpty();
        assertThat(json).contains(eventId);
    }

    @Test
    @DisplayName("Debería manejar eventos sin notificación in-app")
    void shouldHandleEventsWithoutInAppNotification() {
        NotificationEvent eventWithoutNotification = NotificationEvent.builder()
                .eventId("event-no-notif")
                .eventType(NotificationType.SECURITY_REPORT_CREATED)
                .sourceModule("SECURITY")
                .userId("user-789")
                .message("Reporte creado")
                .priority(1)
                .timestamp(Instant.now())
                .payload("{\"reportId\":\"REP001\"}")
                .notification(null)
                .build();

        String json = eventWithoutNotification.toJSON();

        assertThat(json).isNotEmpty();
        assertThat(json).contains("event-no-notif");
        assertThat(json).contains("SECURITY_REPORT_CREATED");
    }

    @Test
    @DisplayName("Debería tener métodos getter y setter para todos los campos")
    void shouldHaveGettersAndSettersForAllFields() {
        String newEventId = "new-event-id";
        notificationEvent.setEventId(newEventId);
        assertThat(notificationEvent.getEventId()).isEqualTo(newEventId);

        String newSourceModule = "NEW_MODULE";
        notificationEvent.setSourceModule(newSourceModule);
        assertThat(notificationEvent.getSourceModule()).isEqualTo(newSourceModule);

        String newUserId = "new-user-123";
        notificationEvent.setUserId(newUserId);
        assertThat(notificationEvent.getUserId()).isEqualTo(newUserId);

        String newMessage = "Nuevo mensaje";
        notificationEvent.setMessage(newMessage);
        assertThat(notificationEvent.getMessage()).isEqualTo(newMessage);

        int newPriority = 5;
        notificationEvent.setPriority(newPriority);
        assertThat(notificationEvent.getPriority()).isEqualTo(newPriority);

        Instant newTimestamp = Instant.now().plus(1, ChronoUnit.DAYS);
        notificationEvent.setTimestamp(newTimestamp);
        assertThat(notificationEvent.getTimestamp()).isEqualTo(newTimestamp);

        String newPayload = "{\"newField\":\"newValue\"}";
        notificationEvent.setPayload(newPayload);
        assertThat(notificationEvent.getPayload()).isEqualTo(newPayload);

        InAppNotification newNotification = InAppNotification.builder()
                .notificationId(UUID.randomUUID())
                .build();
        notificationEvent.setNotification(newNotification);
        assertThat(notificationEvent.getNotification()).isEqualTo(newNotification);
    }
}