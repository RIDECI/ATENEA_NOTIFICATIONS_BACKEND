package edu.dosw.rideci.unit.application.service;

import edu.dosw.rideci.application.port.in.CreateEmergencyBroadcastUseCase;
import edu.dosw.rideci.application.port.out.NotificationRepositoryPort;
import edu.dosw.rideci.application.service.CreateEmergencyBroadcastService;
import edu.dosw.rideci.domain.model.Enum.BroadcastType;
import edu.dosw.rideci.domain.model.Enum.NotificationStatus;
import edu.dosw.rideci.domain.model.Enum.NotificationType;
import edu.dosw.rideci.domain.model.InAppNotification;
import edu.dosw.rideci.domain.service.EmailNotificationSender;
import edu.dosw.rideci.domain.service.NotificationDomainService;
import edu.dosw.rideci.domain.service.UserEmailResolver;
import edu.dosw.rideci.infrastructure.notification.EmailTemplateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateEmergencyBroadcastServiceTest {

    @Mock
    private NotificationRepositoryPort notificationRepositoryPort;

    @Mock
    private NotificationDomainService notificationDomainService;

    @Mock
    private EmailTemplateService emailTemplateService;

    @Mock
    private EmailNotificationSender emailNotificationSender;

    @Mock
    private UserEmailResolver userEmailResolver;

    private CreateEmergencyBroadcastService service;

    @BeforeEach
    void setUp() {
        service = new CreateEmergencyBroadcastService(
                notificationRepositoryPort,
                notificationDomainService,
                emailTemplateService,
                emailNotificationSender,
                userEmailResolver
        );
    }

    @Test
    void createEmergencyBroadcast_ShouldReturnEmptyListWhenCommandIsNull() {
        List<InAppNotification> result = service.createEmergencyBroadcast(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(notificationRepositoryPort, never()).save(any());
    }

    @Test
    void createEmergencyBroadcast_ShouldReturnEmptyListWhenTargetUserIdsIsNull() {
        CreateEmergencyBroadcastUseCase.CreateEmergencyBroadcastCommand command =
                new CreateEmergencyBroadcastUseCase.CreateEmergencyBroadcastCommand(
                        "Emergency message",
                        BroadcastType.EMERGENCY,
                        null,
                        "HIGH"
                );

        List<InAppNotification> result = service.createEmergencyBroadcast(command);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(notificationRepositoryPort, never()).save(any());
    }

    @Test
    void createEmergencyBroadcast_ShouldReturnEmptyListWhenTargetUserIdsIsEmpty() {
        CreateEmergencyBroadcastUseCase.CreateEmergencyBroadcastCommand command =
                new CreateEmergencyBroadcastUseCase.CreateEmergencyBroadcastCommand(
                        "Emergency message",
                        BroadcastType.EMERGENCY,
                        Collections.emptyList(),
                        "HIGH"
                );

        List<InAppNotification> result = service.createEmergencyBroadcast(command);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(notificationRepositoryPort, never()).save(any());
    }

    @Test
    void createEmergencyBroadcast_ShouldCreateNotificationsForValidUsers() {
        String userId1 = UUID.randomUUID().toString();
        String userId2 = UUID.randomUUID().toString();
        CreateEmergencyBroadcastUseCase.CreateEmergencyBroadcastCommand command =
                new CreateEmergencyBroadcastUseCase.CreateEmergencyBroadcastCommand(
                        "Emergency message",
                        BroadcastType.EMERGENCY,
                        Arrays.asList(userId1, userId2),
                        "HIGH"
                );

        when(emailTemplateService.buildAdminBroadcastEmail(any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn("<html>Test HTML</html>");
        when(userEmailResolver.resolveEmail(anyString())).thenReturn("test@example.com");
        when(notificationDomainService.initializeNotification(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(notificationRepositoryPort.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        List<InAppNotification> result = service.createEmergencyBroadcast(command);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(notificationDomainService, times(2)).initializeNotification(any());
        verify(notificationRepositoryPort, times(2)).save(any());
        verify(emailNotificationSender, times(2)).sendNotification(any(), anyString());
    }

    @Test
    void createEmergencyBroadcast_ShouldHandleInvalidUUID() {
        String invalidUserId = "invalid-uuid";
        CreateEmergencyBroadcastUseCase.CreateEmergencyBroadcastCommand command =
                new CreateEmergencyBroadcastUseCase.CreateEmergencyBroadcastCommand(
                        "Emergency message",
                        BroadcastType.EMERGENCY,
                        Arrays.asList(invalidUserId),
                        "HIGH"
                );

        when(emailTemplateService.buildAdminBroadcastEmail(any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn("<html>Test HTML</html>");
        when(userEmailResolver.resolveEmail(anyString())).thenReturn("test@example.com");

        List<InAppNotification> result = service.createEmergencyBroadcast(command);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertNull(result.get(0).getUserId());
        verify(notificationRepositoryPort, never()).save(any());
        verify(emailNotificationSender, times(1)).sendNotification(any(), anyString());
    }

    @Test
    void createEmergencyBroadcast_ShouldHandleExceptionForSingleUser() {
        String userId = UUID.randomUUID().toString();
        CreateEmergencyBroadcastUseCase.CreateEmergencyBroadcastCommand command =
                new CreateEmergencyBroadcastUseCase.CreateEmergencyBroadcastCommand(
                        "Emergency message",
                        BroadcastType.EMERGENCY,
                        Arrays.asList(userId),
                        "HIGH"
                );

        when(emailTemplateService.buildAdminBroadcastEmail(any(), any(), any(), any(), any(), any(), any(), any()))
                .thenThrow(new RuntimeException("Template error"));

        List<InAppNotification> result = service.createEmergencyBroadcast(command);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void createEmergencyBroadcast_ShouldUseCorrectBroadcastType() {
        String userId = UUID.randomUUID().toString();
        CreateEmergencyBroadcastUseCase.CreateEmergencyBroadcastCommand command =
                new CreateEmergencyBroadcastUseCase.CreateEmergencyBroadcastCommand(
                        "Emergency message",
                        BroadcastType.SYSTEM_ANNOUNCEMENT,
                        Arrays.asList(userId),
                        "NORMAL"
                );

        when(emailTemplateService.buildAdminBroadcastEmail(any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn("<html>Test HTML</html>");
        when(userEmailResolver.resolveEmail(anyString())).thenReturn("test@example.com");
        when(notificationDomainService.initializeNotification(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(notificationRepositoryPort.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        List<InAppNotification> result = service.createEmergencyBroadcast(command);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Anuncio del sistema", result.get(0).getTitle());
        assertEquals(NotificationType.EMERGENCY_ALERT, result.get(0).getEventType());
        assertEquals("NORMAL", result.get(0).getPriority());
    }

    @Test
    void createEmergencyBroadcast_ShouldUseDefaultPriorityWhenNull() {
        String userId = UUID.randomUUID().toString();
        CreateEmergencyBroadcastUseCase.CreateEmergencyBroadcastCommand command =
                new CreateEmergencyBroadcastUseCase.CreateEmergencyBroadcastCommand(
                        "Emergency message",
                        BroadcastType.EMERGENCY,
                        Arrays.asList(userId),
                        null
                );

        when(emailTemplateService.buildAdminBroadcastEmail(any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn("<html>Test HTML</html>");
        when(userEmailResolver.resolveEmail(anyString())).thenReturn("test@example.com");
        when(notificationDomainService.initializeNotification(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(notificationRepositoryPort.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        List<InAppNotification> result = service.createEmergencyBroadcast(command);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("HIGH", result.get(0).getPriority());
    }

    @Test
    void buildBroadcastTitle_ShouldReturnCorrectTitleForAllTypes() {
        String userId = UUID.randomUUID().toString();
        List<String> userIds = Arrays.asList(userId);

        when(emailTemplateService.buildAdminBroadcastEmail(any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn("<html>Test HTML</html>");
        when(userEmailResolver.resolveEmail(anyString())).thenReturn("test@example.com");
        when(notificationDomainService.initializeNotification(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(notificationRepositoryPort.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Test EMERGENCY
        CreateEmergencyBroadcastUseCase.CreateEmergencyBroadcastCommand command1 =
                new CreateEmergencyBroadcastUseCase.CreateEmergencyBroadcastCommand(
                        "Message", BroadcastType.EMERGENCY, userIds, "HIGH");
        List<InAppNotification> result1 = service.createEmergencyBroadcast(command1);
        assertEquals("Alerta de emergencia", result1.get(0).getTitle());

        // Test HIGH_PRIORITY_ALERT
        CreateEmergencyBroadcastUseCase.CreateEmergencyBroadcastCommand command2 =
                new CreateEmergencyBroadcastUseCase.CreateEmergencyBroadcastCommand(
                        "Message", BroadcastType.HIGH_PRIORITY_ALERT, userIds, "HIGH");
        List<InAppNotification> result2 = service.createEmergencyBroadcast(command2);
        assertEquals("Alerta prioritaria", result2.get(0).getTitle());

        // Test SYSTEM_ANNOUNCEMENT
        CreateEmergencyBroadcastUseCase.CreateEmergencyBroadcastCommand command3 =
                new CreateEmergencyBroadcastUseCase.CreateEmergencyBroadcastCommand(
                        "Message", BroadcastType.SYSTEM_ANNOUNCEMENT, userIds, "HIGH");
        List<InAppNotification> result3 = service.createEmergencyBroadcast(command3);
        assertEquals("Anuncio del sistema", result3.get(0).getTitle());

        // Test MAINTENANCE_NOTICE
        CreateEmergencyBroadcastUseCase.CreateEmergencyBroadcastCommand command4 =
                new CreateEmergencyBroadcastUseCase.CreateEmergencyBroadcastCommand(
                        "Message", BroadcastType.MAINTENANCE_NOTICE, userIds, "HIGH");
        List<InAppNotification> result4 = service.createEmergencyBroadcast(command4);
        assertEquals("Aviso de mantenimiento", result4.get(0).getTitle());

        // Test ADMIN_ANNOUNCEMENT
        CreateEmergencyBroadcastUseCase.CreateEmergencyBroadcastCommand command5 =
                new CreateEmergencyBroadcastUseCase.CreateEmergencyBroadcastCommand(
                        "Message", BroadcastType.ADMIN_ANNOUNCEMENT, userIds, "HIGH");
        List<InAppNotification> result5 = service.createEmergencyBroadcast(command5);
        assertEquals("Comunicado administrativo", result5.get(0).getTitle());

        // Test TEST_BROADCAST
        CreateEmergencyBroadcastUseCase.CreateEmergencyBroadcastCommand command6 =
                new CreateEmergencyBroadcastUseCase.CreateEmergencyBroadcastCommand(
                        "Message", BroadcastType.TEST_BROADCAST, userIds, "HIGH");
        List<InAppNotification> result6 = service.createEmergencyBroadcast(command6);
        assertEquals("Mensaje de prueba", result6.get(0).getTitle());
    }
}
