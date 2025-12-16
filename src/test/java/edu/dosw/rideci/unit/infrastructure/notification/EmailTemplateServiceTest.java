package edu.dosw.rideci.unit.infrastructure.notification;

import edu.dosw.rideci.application.port.in.SendEmailNotificationUseCase;
import edu.dosw.rideci.infrastructure.notification.EmailTemplateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EmailTemplateServiceTest {

    private EmailTemplateService templateService;

    @BeforeEach
    void setUp() {
        templateService = new EmailTemplateService();
    }

    @Test
    void buildPasswordRecoveryEmail_ShouldGenerateValidHTML() {
        String html = templateService.buildPasswordRecoveryEmail(
                "Usuario Test",
                "RideECI",
                "ABC123",
                30
        );

        assertNotNull(html);
        assertTrue(html.contains("ABC123"));
        assertTrue(html.contains("RideECI"));
        assertTrue(html.contains("30"));
        assertTrue(html.contains("<html>"));
        assertTrue(html.contains("</html>"));
    }

    @Test
    void buildPasswordRecoveryEmail_WithCommand_ShouldGenerateValidHTML() {
        SendEmailNotificationUseCase.SendEmailNotificationCommand command =
                SendEmailNotificationUseCase.SendEmailNotificationCommand.builder()
                        .reason("RESET456")
                        .build();

        String html = templateService.buildPasswordRecoveryEmail(command);

        assertNotNull(html);
        assertTrue(html.contains("RESET456"));
        assertTrue(html.contains("RideECI"));
    }

    @Test
    void buildPasswordRecoveryEmail_WithCommandNullReason_ShouldUseDefault() {
        SendEmailNotificationUseCase.SendEmailNotificationCommand command =
                SendEmailNotificationUseCase.SendEmailNotificationCommand.builder()
                        .reason(null)
                        .build();

        String html = templateService.buildPasswordRecoveryEmail(command);

        assertNotNull(html);
        assertTrue(html.contains("Código no disponible"));
    }

    @Test
    void buildRegistrationVerificationEmail_ShouldGenerateValidHTML() {
        String html = templateService.buildRegistrationVerificationEmail(
                "Usuario Test",
                "RideECI",
                "https://example.com/verify"
        );

        assertNotNull(html);
        assertTrue(html.contains("Usuario Test"));
        assertTrue(html.contains("RideECI"));
        assertTrue(html.contains("https://example.com/verify"));
        assertTrue(html.contains("Verificar mi cuenta"));
    }

    @Test
    void buildRegistrationVerificationEmail_WithCommand_ShouldGenerateValidHTML() {
        SendEmailNotificationUseCase.SendEmailNotificationCommand command =
                SendEmailNotificationUseCase.SendEmailNotificationCommand.builder()
                        .reason("https://example.com/verify")
                        .extraInfo("Usuario Test")
                        .build();

        String html = templateService.buildRegistrationVerificationEmail(command);

        assertNotNull(html);
        assertTrue(html.contains("Usuario Test"));
        assertTrue(html.contains("https://example.com/verify"));
    }

    @Test
    void buildAccountSuspensionEmail_ShouldGenerateValidHTMLForPermanent() {
        UUID userId = UUID.randomUUID();
        String html = templateService.buildAccountSuspensionEmail(
                userId,
                "Violación de términos",
                true
        );

        assertNotNull(html);
        assertTrue(html.contains(userId.toString()));
        assertTrue(html.contains("suspendida de forma permanente"));
        assertTrue(html.contains("Violación de términos"));
    }

    @Test
    void buildAccountSuspensionEmail_ShouldGenerateValidHTMLForTemporary() {
        UUID userId = UUID.randomUUID();
        String html = templateService.buildAccountSuspensionEmail(
                userId,
                "Suspensión temporal",
                false
        );

        assertNotNull(html);
        assertTrue(html.contains(userId.toString()));
        assertTrue(html.contains("suspendida temporalmente"));
        assertTrue(html.contains("Suspensión temporal"));
    }

    @Test
    void buildAccountSuspensionEmail_WithCommand_ShouldGenerateValidHTML() {
        UUID userId = UUID.randomUUID();
        SendEmailNotificationUseCase.SendEmailNotificationCommand command =
                SendEmailNotificationUseCase.SendEmailNotificationCommand.builder()
                        .userId(userId)
                        .reason("Violación de términos")
                        .build();

        String html = templateService.buildAccountSuspensionEmail(command);

        assertNotNull(html);
        assertTrue(html.contains(userId.toString()));
    }

    @Test
    void buildDriverVerificationResultEmail_ShouldGenerateValidHTMLForApproved() {
        String html = templateService.buildDriverVerificationResultEmail(
                "Conductor Test",
                "RideECI",
                true,
                null
        );

        assertNotNull(html);
        assertTrue(html.contains("Conductor Test"));
        assertTrue(html.contains("APROBADO"));
        assertTrue(html.contains("Felicidades"));
    }

    @Test
    void buildDriverVerificationResultEmail_ShouldGenerateValidHTMLForRejected() {
        String html = templateService.buildDriverVerificationResultEmail(
                "Conductor Test",
                "RideECI",
                false,
                "Documentación incompleta"
        );

        assertNotNull(html);
        assertTrue(html.contains("Conductor Test"));
        assertTrue(html.contains("RECHAZADO"));
        assertTrue(html.contains("Documentación incompleta"));
    }

    @Test
    void buildDriverVerificationResultEmail_WithCommand_ShouldGenerateValidHTML() {
        SendEmailNotificationUseCase.SendEmailNotificationCommand command =
                SendEmailNotificationUseCase.SendEmailNotificationCommand.builder()
                        .extraInfo("Conductor Test")
                        .reason("APROBADO")
                        .build();

        String html = templateService.buildDriverVerificationResultEmail(command);

        assertNotNull(html);
        assertTrue(html.contains("Conductor Test"));
    }

    @Test
    void buildTripBookingConfirmationEmail_ShouldGenerateValidHTML() {
        String html = templateService.buildTripBookingConfirmationEmail(
                "Usuario Test",
                "RideECI",
                "trip-123",
                "2024-01-01T10:00:00Z",
                "Origen",
                "Destino",
                "Conductor Test",
                "Sedan ABC123"
        );

        assertNotNull(html);
        assertTrue(html.contains("Usuario Test"));
        assertTrue(html.contains("trip-123"));
        assertTrue(html.contains("Origen"));
        assertTrue(html.contains("Destino"));
        assertTrue(html.contains("Conductor Test"));
        assertTrue(html.contains("Sedan ABC123"));
    }

    @Test
    void buildTripBookingConfirmationEmail_WithCommand_ShouldGenerateValidHTML() {
        SendEmailNotificationUseCase.SendEmailNotificationCommand command =
                SendEmailNotificationUseCase.SendEmailNotificationCommand.builder()
                        .tripId("trip-123")
                        .scheduledAt(OffsetDateTime.now())
                        .extraInfo("Usuario Test")
                        .build();

        String html = templateService.buildTripBookingConfirmationEmail(command);

        assertNotNull(html);
        assertTrue(html.contains("trip-123"));
    }

    @Test
    void buildTripUpdateEmail_ShouldGenerateValidHTMLForCancellation() {
        String html = templateService.buildTripUpdateEmail(
                "Usuario Test",
                "RideECI",
                "trip-123",
                "CANCELACION",
                null,
                null,
                null,
                "Razón de cancelación"
        );

        assertNotNull(html);
        assertTrue(html.contains("Usuario Test"));
        assertTrue(html.contains("trip-123"));
        assertTrue(html.contains("Cancelación"));
        assertTrue(html.contains("Razón de cancelación"));
    }

    @Test
    void buildTripUpdateEmail_ShouldGenerateValidHTMLForModification() {
        String html = templateService.buildTripUpdateEmail(
                "Usuario Test",
                "RideECI",
                "trip-123",
                "MODIFICACION",
                "2024-01-01T11:00:00Z",
                "Nuevo Origen",
                "Nuevo Destino",
                null
        );

        assertNotNull(html);
        assertTrue(html.contains("Usuario Test"));
        assertTrue(html.contains("trip-123"));
        assertTrue(html.contains("Modificación"));
    }

    @Test
    void buildTripUpdateEmail_WithCommand_ShouldGenerateValidHTML() {
        SendEmailNotificationUseCase.SendEmailNotificationCommand command =
                SendEmailNotificationUseCase.SendEmailNotificationCommand.builder()
                        .tripId("trip-123")
                        .reason("Cancelación por conductor")
                        .extraInfo("Usuario Test")
                        .build();

        String html = templateService.buildTripUpdateEmail(command);

        assertNotNull(html);
        assertTrue(html.contains("trip-123"));
    }

    @Test
    void buildTripReminderEmail_ShouldGenerateValidHTML() {
        String html = templateService.buildTripReminderEmail(
                "Usuario Test",
                "RideECI",
                "trip-123",
                "2024-01-01T10:00:00Z",
                "Origen",
                "Destino"
        );

        assertNotNull(html);
        assertTrue(html.contains("Usuario Test"));
        assertTrue(html.contains("trip-123"));
        assertTrue(html.contains("Origen"));
        assertTrue(html.contains("Destino"));
    }

    @Test
    void buildTripReminderEmail_WithCommand_ShouldGenerateValidHTML() {
        SendEmailNotificationUseCase.SendEmailNotificationCommand command =
                SendEmailNotificationUseCase.SendEmailNotificationCommand.builder()
                        .tripId("trip-123")
                        .scheduledAt(OffsetDateTime.now())
                        .extraInfo("Usuario Test")
                        .build();

        String html = templateService.buildTripReminderEmail(command);

        assertNotNull(html);
        assertTrue(html.contains("trip-123"));
    }

    @Test
    void buildPaymentConfirmationEmail_ShouldGenerateValidHTML() {
        String html = templateService.buildPaymentConfirmationEmail(
                "Usuario Test",
                "RideECI",
                "payment-123",
                "$50.00",
                "Tarjeta de crédito",
                "trip-123"
        );

        assertNotNull(html);
        assertTrue(html.contains("Usuario Test"));
        assertTrue(html.contains("payment-123"));
        assertTrue(html.contains("$50.00"));
        assertTrue(html.contains("trip-123"));
    }

    @Test
    void buildPaymentConfirmationEmail_WithCommand_ShouldGenerateValidHTML() {
        SendEmailNotificationUseCase.SendEmailNotificationCommand command =
                SendEmailNotificationUseCase.SendEmailNotificationCommand.builder()
                        .paymentId("payment-123")
                        .tripId("trip-123")
                        .reason("$50.00")
                        .extraInfo("Usuario Test")
                        .build();

        String html = templateService.buildPaymentConfirmationEmail(command);

        assertNotNull(html);
        assertTrue(html.contains("payment-123"));
    }

    @Test
    void buildEmergencyAlertEmail_ShouldGenerateValidHTML() {
        String html = templateService.buildEmergencyAlertEmail(
                "Usuario Test",
                "RideECI",
                "trip-123",
                "Descripción del incidente"
        );

        assertNotNull(html);
        assertTrue(html.contains("Usuario Test"));
        assertTrue(html.contains("trip-123"));
        assertTrue(html.contains("Descripción del incidente"));
        assertTrue(html.contains("botón de emergencia"));
    }

    @Test
    void buildEmergencyAlertEmail_WithCommand_ShouldGenerateValidHTML() {
        SendEmailNotificationUseCase.SendEmailNotificationCommand command =
                SendEmailNotificationUseCase.SendEmailNotificationCommand.builder()
                        .tripId("trip-123")
                        .reason("Emergencia reportada")
                        .extraInfo("Usuario Test")
                        .build();

        String html = templateService.buildEmergencyAlertEmail(command);

        assertNotNull(html);
        assertTrue(html.contains("trip-123"));
    }

    @Test
    void buildAdminBroadcastEmail_ShouldGenerateValidHTML() {
        String html = templateService.buildAdminBroadcastEmail(
                "Usuario Test",
                "RideECI",
                "Título del broadcast",
                "Mensaje principal",
                "Motivo del broadcast",
                "EMERGENCY",
                "HIGH",
                OffsetDateTime.now()
        );

        assertNotNull(html);
        assertTrue(html.contains("Usuario Test"));
        assertTrue(html.contains("Título del broadcast"));
        assertTrue(html.contains("Mensaje principal"));
        assertTrue(html.contains("EMERGENCY"));
        assertTrue(html.contains("HIGH"));
    }

    @Test
    void buildAdminBroadcastEmail_WithNullValues_ShouldUseDefaults() {
        String html = templateService.buildAdminBroadcastEmail(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );

        assertNotNull(html);
        assertTrue(html.contains("Usuario"));
        assertTrue(html.contains("RideECI"));
    }

    @Test
    void buildTravelCreatedEmail_ShouldGenerateValidHTML() {
        String html = templateService.buildTravelCreatedEmail(
                "Usuario Test",
                "RideECI",
                "Origen",
                "Destino",
                "2024-01-01T10:00:00Z",
                4,
                50.0
        );

        assertNotNull(html);
        assertTrue(html.contains("Usuario Test"));
        assertTrue(html.contains("Origen"));
        assertTrue(html.contains("Destino"));
        assertTrue(html.contains("4 cupos disponibles"));
        assertTrue(html.contains("$50.00"));
    }

    @Test
    void buildTravelUpdatedEmail_ShouldGenerateValidHTML() {
        String html = templateService.buildTravelUpdatedEmail(
                "Usuario Test",
                "RideECI",
                "travel-123",
                "ACTIVO",
                "Cambio de horario",
                "Nueva hora de salida: 11:00 AM"
        );

        assertNotNull(html);
        assertTrue(html.contains("Usuario Test"));
        assertTrue(html.contains("travel-123"));
        assertTrue(html.contains("ACTIVO"));
        assertTrue(html.contains("Cambio de horario"));
    }

    @Test
    void buildTravelCancelledEmail_ShouldGenerateValidHTML() {
        String html = templateService.buildTravelCancelledEmail(
                "Usuario Test",
                "RideECI",
                "travel-123",
                "Origen",
                "Destino",
                "Razón de cancelación",
                "DRIVER",
                "Reembolso completo"
        );

        assertNotNull(html);
        assertTrue(html.contains("Usuario Test"));
        assertTrue(html.contains("travel-123"));
        assertTrue(html.contains("Origen"));
        assertTrue(html.contains("Destino"));
        assertTrue(html.contains("Razón de cancelación"));
        assertTrue(html.contains("el conductor"));
    }
}
