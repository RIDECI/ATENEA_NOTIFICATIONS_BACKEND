package edu.dosw.rideci.infrastructure.notification;

import edu.dosw.rideci.application.port.in.SendEmailNotificationUseCase;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
public class EmailTemplateService {

    // ============================================================
    //  HELPERS
    // ============================================================

    private String defaultUserName(SendEmailNotificationUseCase.SendEmailNotificationCommand command) {
        // Ajusta esto cuando tengas el nombre real del usuario:
        return (command.extraInfo() != null && !command.extraInfo().isBlank())
                ? command.extraInfo()
                : "Usuario";
    }

    private String defaultAppName() {
        return "RideECI";
    }

    // ============================================================
    //  SEGURIDAD DE CUENTA: RECUPERACIÓN Y REGISTRO
    // ============================================================

    public String buildPasswordRecoveryEmail(String userName, String appName,
                                             String recoveryCode, int expirationMinutes) {
        return """
        <html>
          <body>
            <p>Hola Usuario de RideECI,</p>
            <p>
              Hemos recibido una solicitud para restablecer la contraseña de tu cuenta
              en <strong>%s</strong>.
            </p>
            <p>
              Si fuiste tú quien solicitó el cambio, usa el siguiente código
              para crear una nueva contraseña:
            </p>

            <!-- AQUÍ VA EL CÓDIGO EN VEZ DEL BOTÓN -->
            <p style="text-align:center; font-size:24px;">
              <strong>%s</strong>
            </p>

            <p>
              Por seguridad, este código expirará en %d minutos. Si no solicitaste
              este cambio, puedes ignorar este mensaje.
            </p>
            <p>
              Saludos,<br/>
              El equipo de %s
            </p>
          </body>
        </html>
        """.formatted(appName, recoveryCode, expirationMinutes, appName);
    }

    public String buildPasswordRecoveryEmail(SendEmailNotificationUseCase.SendEmailNotificationCommand command) {
        // Usamos el nombre genérico directamente en lugar de defaultUserName
        String appName = defaultAppName();

        // De momento usamos reason como link / token (ajusta en tu endpoint)
        String recoveryCode = command.reason() != null ? command.reason() : "Código no disponible";
        int expirationMinutes = 30;

        return buildPasswordRecoveryEmail("Usuario de RideECI", appName, recoveryCode, expirationMinutes);
    }

    public String buildRegistrationVerificationEmail(String userName, String appName,
                                                     String verificationLink) {
        return """
            <html>
              <body>
                <p>Hola %s,</p>
                <p>
                  ¡Gracias por registrarte en <strong>%s</strong>! Para completar tu registro
                  y activar tu cuenta, por favor verifica tu correo electrónico.
                </p>
                <p style="text-align:center;">
                  <a href="%s"
                     style="background-color:#28a745;color:#ffffff;padding:10px 20px;
                            text-decoration:none;border-radius:4px;display:inline-block;">
                    Verificar mi cuenta
                  </a>
                </p>
                <p>
                  Si no creaste una cuenta en %s, simplemente ignora este correo.
                </p>
                <p>
                  Bienvenido a bordo,<br/>
                  El equipo de %s
                </p>
              </body>
            </html>
            """.formatted(userName, appName, verificationLink, appName, appName);
    }

    public String buildRegistrationVerificationEmail(SendEmailNotificationUseCase.SendEmailNotificationCommand command) {
        String userName = defaultUserName(command);
        String appName = defaultAppName();

        // De momento reason como link de verificación (ajusta en tu endpoint)
        String verificationLink = command.reason() != null ? command.reason() : "https://example.com/verify";

        return buildRegistrationVerificationEmail(userName, appName, verificationLink);
    }

    // ============================================================
    //  SUSPENSIÓN DE CUENTA
    // ============================================================

    public String buildAccountSuspensionEmail(UUID userId, String reason,
                                              boolean permanent) {

        String estado = permanent
                ? "suspendida de forma permanente"
                : "suspendida temporalmente";

        String extra = permanent
                ? """
                   <p>
                     Si consideras que se trata de un error, por favor comunícate con
                     nuestro equipo de soporte para revisar tu caso.
                   </p>
                   """
                : """
                   <p>
                     En caso de que tengas dudas o desees apelar esta decisión, puedes
                     contactar a nuestro equipo de soporte desde los canales oficiales.
                   </p>
                   """;

        String reasonBlock = (reason != null && !reason.isBlank())
                ? "<p><strong>Motivo:</strong> " + reason + "</p>"
                : "";

        return """
            <html>
              <body>
                <p>Hola,</p>
                <p>
                  Te informamos que la cuenta asociada al ID de usuario
                  <strong>%s</strong> ha sido %s.
                </p>
                %s
                %s
                <p>
                  Saludos,<br/>
                  El equipo de la plataforma
                </p>
              </body>
            </html>
            """.formatted(
                userId,
                estado,
                reasonBlock,
                extra
        );
    }

    public String buildAccountSuspensionEmail(SendEmailNotificationUseCase.SendEmailNotificationCommand command) {
        UUID userId = command.userId();
        String reason = command.reason();
        boolean permanent = true;

        return buildAccountSuspensionEmail(userId, reason, permanent);
    }

    // ============================================================
    //  VERIFICACIÓN DE CONDUCTOR
    // ============================================================

    public String buildDriverVerificationResultEmail(String driverName, String appName,
                                                     boolean approved, String reason) {
        String estado = approved ? "APROBADO" : "RECHAZADO";

        String extra = approved
                ? """
                   <p>
                     ¡Felicidades! A partir de ahora podrás usar la plataforma como conductor
                     y recibir solicitudes de viaje.
                   </p>
                   """
                : """
                   <p>
                     Lamentablemente, en esta ocasión tu verificación no ha sido aprobada.
                   </p>
                   %s
                   <p>
                     Si consideras que se trata de un error o tienes documentación adicional,
                     puedes comunicarte con nuestro equipo de soporte.
                   </p>
                   """.formatted(
                (reason != null && !reason.isBlank())
                        ? "<p><strong>Motivo:</strong> " + reason + "</p>"
                        : ""
        );

        return """
            <html>
              <body>
                <p>Hola %s,</p>
                <p>
                  Te informamos que tu proceso de verificación como conductor en
                  <strong>%s</strong> ha sido <strong>%s</strong>.
                </p>
                %s
                <p>
                  Gracias por tu interés en conducir con nosotros.<br/>
                  El equipo de %s
                </p>
              </body>
            </html>
            """.formatted(driverName, appName, estado, extra, appName);
    }

    public String buildDriverVerificationResultEmail(SendEmailNotificationUseCase.SendEmailNotificationCommand command) {
        String driverName = defaultUserName(command);
        String appName = defaultAppName();

        // Puedes codificar en reason si fue aprobado/rechazado y el motivo
        boolean approved = !"RECHAZADO".equalsIgnoreCase(command.reason());
        String reason = command.reason();

        return buildDriverVerificationResultEmail(driverName, appName, approved, reason);
    }

    // ============================================================
    //  CICLO DE VIAJE: RESERVA, ACTUALIZACIÓN, RECORDATORIO
    // ============================================================

    public String buildTripBookingConfirmationEmail(String userName, String appName,
                                                    String tripId, String dateTime,
                                                    String origin, String destination,
                                                    String driverName, String vehicleInfo) {

        String driverBlock = (driverName != null && !driverName.isBlank())
                ? "<li><strong>Conductor:</strong> " + driverName + "</li>"
                : "";

        String vehicleBlock = (vehicleInfo != null && !vehicleInfo.isBlank())
                ? "<li><strong>Vehículo:</strong> " + vehicleInfo + "</li>"
                : "";

        return """
            <html>
              <body>
                <p>Hola %s,</p>
                <p>
                  Tu reserva de viaje ha sido <strong>confirmada</strong> en
                  <strong>%s</strong>.
                </p>
                <p><strong>Detalles del viaje:</strong></p>
                <ul>
                  <li><strong>ID del viaje:</strong> %s</li>
                  <li><strong>Fecha y hora:</strong> %s</li>
                  <li><strong>Origen:</strong> %s</li>
                  <li><strong>Destino:</strong> %s</li>
                  %s
                  %s
                </ul>
                <p>
                  Podrás seguir el estado de tu viaje directamente desde la aplicación.
                </p>
                <p>
                  Gracias por viajar con nosotros,<br/>
                  El equipo de %s
                </p>
              </body>
            </html>
            """.formatted(
                userName,
                appName,
                tripId,
                dateTime,
                origin,
                destination,
                driverBlock,
                vehicleBlock,
                appName
        );
    }

    public String buildTripBookingConfirmationEmail(SendEmailNotificationUseCase.SendEmailNotificationCommand command) {
        String userName = defaultUserName(command);
        String appName = defaultAppName();

        String tripId = command.tripId();
        OffsetDateTime dt = command.scheduledAt();
        String dateTime = dt != null ? dt.toString() : "Fecha y hora no disponibles";

        // Por ahora no tenemos origen/destino/vehículo en el command
        String origin = "Origen no disponible";
        String destination = "Destino no disponible";
        String driverName = null;
        String vehicleInfo = null;

        return buildTripBookingConfirmationEmail(
                userName, appName, tripId, dateTime,
                origin, destination, driverName, vehicleInfo
        );
    }

    public String buildTripUpdateEmail(String userName, String appName,
                                       String tripId, String updateType,
                                       String newDateTime, String origin,
                                       String destination, String cancelReason) {

        boolean isCancellation = "CANCELACION".equalsIgnoreCase(updateType)
                || "CANCELACIÓN".equalsIgnoreCase(updateType);

        String updateText = isCancellation ? "Cancelación" : "Modificación";

        String detailsBlock = isCancellation
                ? (cancelReason != null && !cancelReason.isBlank()
                ? "<p><strong>Motivo de la cancelación:</strong> " + cancelReason + "</p>"
                : "")
                : """
                  <p><strong>Nuevos detalles del viaje:</strong></p>
                  <ul>
                    %s
                    %s
                    %s
                  </ul>
                  """.formatted(
                (newDateTime != null && !newDateTime.isBlank())
                        ? "<li><strong>Nueva fecha y hora:</strong> " + newDateTime + "</li>" : "",
                (origin != null && !origin.isBlank())
                        ? "<li><strong>Origen:</strong> " + origin + "</li>" : "",
                (destination != null && !destination.isBlank())
                        ? "<li><strong>Destino:</strong> " + destination + "</li>" : ""
        );

        return """
            <html>
              <body>
                <p>Hola %s,</p>
                <p>
                  Te informamos que tu viaje con ID <strong>%s</strong> ha tenido la siguiente
                  actualización: <strong>%s</strong>.
                </p>
                %s
                <p>
                  Si tienes dudas o necesitas más información, puedes contactar a nuestro
                  equipo de soporte desde la aplicación.
                </p>
                <p>
                  Saludos,<br/>
                  El equipo de %s
                </p>
              </body>
            </html>
            """.formatted(
                userName,
                tripId,
                updateText,
                detailsBlock,
                appName
        );
    }

    public String buildTripUpdateEmail(SendEmailNotificationUseCase.SendEmailNotificationCommand command) {
        String userName = defaultUserName(command);
        String appName = defaultAppName();

        String tripId = command.tripId();
        String updateType = (command.reason() != null && command.reason().toLowerCase().contains("cancel"))
                ? "CANCELACION"
                : "MODIFICACION";

        OffsetDateTime dt = command.scheduledAt();
        String newDateTime = dt != null ? dt.toString() : null;

        // Por ahora sin origen/destino reales:
        String origin = "Origen no disponible";
        String destination = "Destino no disponible";

        String cancelReason = command.reason();

        return buildTripUpdateEmail(
                userName, appName, tripId, updateType,
                newDateTime, origin, destination, cancelReason
        );
    }

    public String buildTripReminderEmail(String userName, String appName,
                                         String tripId, String dateTime,
                                         String origin, String destination) {
        return """
            <html>
              <body>
                <p>Hola %s,</p>
                <p>
                  Este es un recordatorio de tu próximo viaje en
                  <strong>%s</strong>.
                </p>
                <ul>
                  <li><strong>ID del viaje:</strong> %s</li>
                  <li><strong>Fecha y hora:</strong> %s</li>
                  <li><strong>Origen:</strong> %s</li>
                  <li><strong>Destino:</strong> %s</li>
                </ul>
                <p>
                  Te recomendamos estar listo unos minutos antes de la hora programada.
                </p>
                <p>
                  Gracias por viajar con nosotros,<br/>
                  El equipo de %s
                </p>
              </body>
            </html>
            """.formatted(
                userName,
                appName,
                tripId,
                dateTime,
                origin,
                destination,
                appName
        );
    }

    public String buildTripReminderEmail(SendEmailNotificationUseCase.SendEmailNotificationCommand command) {
        String userName = defaultUserName(command);
        String appName = defaultAppName();

        String tripId = command.tripId();
        OffsetDateTime dt = command.scheduledAt();
        String dateTime = dt != null ? dt.toString() : "Fecha y hora no disponibles";

        String origin = "Origen no disponible";
        String destination = "Destino no disponible";

        return buildTripReminderEmail(
                userName, appName, tripId, dateTime, origin, destination
        );
    }

    // ============================================================
    //  PAGO
    // ============================================================

    public String buildPaymentConfirmationEmail(String userName, String appName,
                                                String paymentId, String amount,
                                                String method, String tripId) {

        String tripBlock = (tripId != null && !tripId.isBlank())
                ? "<li><strong>ID del viaje:</strong> " + tripId + "</li>"
                : "";

        return """
            <html>
              <body>
                <p>Hola %s,</p>
                <p>
                  Hemos recibido tu pago en <strong>%s</strong>.
                </p>
                <p><strong>Detalles del pago:</strong></p>
                <ul>
                  %s
                  <li><strong>ID de pago:</strong> %s</li>
                  <li><strong>Monto:</strong> %s</li>
                  <li><strong>Método de pago:</strong> %s</li>
                </ul>
                <p>
                  Gracias por usar nuestra plataforma.
                </p>
                <p>
                  Saludos,<br/>
                  El equipo de %s
                </p>
              </body>
            </html>
            """.formatted(
                userName,
                appName,
                tripBlock,
                paymentId,
                amount,
                method,
                appName
        );
    }

    public String buildPaymentConfirmationEmail(SendEmailNotificationUseCase.SendEmailNotificationCommand command) {
        String userName = defaultUserName(command);
        String appName = defaultAppName();

        String paymentId = command.paymentId();
        String tripId = command.tripId();

        // Por ahora reason como monto
        String amount = (command.reason() != null && !command.reason().isBlank())
                ? command.reason()
                : "Monto no disponible";

        String method = "Método de pago";

        return buildPaymentConfirmationEmail(
                userName, appName, paymentId, amount, method, tripId
        );
    }

    // ============================================================
    //  EMERGENCIA
    // ============================================================

    public String buildEmergencyAlertEmail(String userName, String appName,
                                           String tripId, String description) {

        String tripBlock = (tripId != null && !tripId.isBlank())
                ? "<p><strong>ID del viaje asociado:</strong> " + tripId + "</p>"
                : "";

        String descBlock = (description != null && !description.isBlank())
                ? "<p><strong>Descripción del incidente:</strong> " + description + "</p>"
                : "";

        return """
            <html>
              <body>
                <p>Hola %s,</p>
                <p>
                  Se ha registrado la activación del <strong>botón de emergencia</strong>
                  en tu cuenta de %s.
                </p>
                %s
                %s
                <p>
                  Nuestro equipo de soporte está revisando la situación. Si necesitas ayuda
                  inmediata, utiliza los canales de emergencia disponibles en tu zona.
                </p>
                <p>
                  Saludos,<br/>
                  El equipo de %s
                </p>
              </body>
            </html>
            """.formatted(
                userName,
                appName,
                tripBlock,
                descBlock,
                appName
        );
    }

    public String buildEmergencyAlertEmail(SendEmailNotificationUseCase.SendEmailNotificationCommand command) {
        String userName = defaultUserName(command);
        String appName = defaultAppName();

        String tripId = command.tripId();
        String description = command.reason() != null ? command.reason() : command.extraInfo();

        return buildEmergencyAlertEmail(userName, appName, tripId, description);
    }

    // ============================================================
    //  BROADCAST ADMINISTRATIVO
    // ============================================================

    /**
     * Plantilla para correos de broadcast administrativo / eventos extraordinarios.
     *
     * Los campos (título, mensaje, motivo, tipo, prioridad, fecha, etc.) los arma
     * el módulo de administración; aquí solo se formatea el HTML.
     *
     * @param userName            Nombre del destinatario (o "Usuario" si no lo tienes).
     * @param appName             Nombre de la aplicación (RideECI, Atenea, etc.).
     * @param title               Título lógico del broadcast (ej: "Alerta de seguridad").
     * @param mainMessage         Mensaje principal que verá el usuario.
     * @param reason              Motivo o contexto de por qué se envía este broadcast.
     * @param broadcastTypeLabel  Texto amigable del tipo de broadcast
     *                             (ej: "EMERGENCY", "SYSTEM_ANNOUNCEMENT"... ya convertido a texto).
     * @param priorityLabel       Texto de prioridad (ej: "ALTA", "MEDIA", "BAJA").
     * @param createdAt           Momento en que se generó este broadcast.
     * @return                    HTML listo para enviar como cuerpo del correo.
     */
    public String buildAdminBroadcastEmail(
            String userName,
            String appName,
            String title,
            String mainMessage,
            String reason,
            String broadcastTypeLabel,
            String priorityLabel,
            OffsetDateTime createdAt
    ) {
        String safeUserName = (userName != null && !userName.isBlank()) ? userName : "Usuario";
        String safeAppName  = (appName != null && !appName.isBlank()) ? appName : defaultAppName();

        String reasonBlock = (reason != null && !reason.isBlank())
                ? "<p><strong>Motivo del mensaje:</strong> " + reason + "</p>"
                : "";

        String typeBlock = (broadcastTypeLabel != null && !broadcastTypeLabel.isBlank())
                ? "<li><strong>Tipo de aviso:</strong> " + broadcastTypeLabel + "</li>"
                : "";

        String priorityBlock = (priorityLabel != null && !priorityLabel.isBlank())
                ? "<li><strong>Prioridad:</strong> " + priorityLabel + "</li>"
                : "";

        String dateBlock = (createdAt != null)
                ? "<li><strong>Fecha del aviso:</strong> " + createdAt.toString() + "</li>"
                : "";

        return """
            <html>
              <body>
                <p>Hola %s,</p>
                <p>
                  Has recibido un <strong>aviso importante</strong> de la administraci&oacute;n
                  de <strong>%s</strong>.
                </p>
                <p><strong>%s</strong></p>
                <p>%s</p>
                %s
                <p><strong>Detalles del aviso:</strong></p>
                <ul>
                  %s
                  %s
                  %s
                </ul>
                <p>
                  Este mensaje fue generado por el m&oacute;dulo de administraci&oacute;n
                  para informar sobre un evento extraordinario en la plataforma.
                </p>
                <p>
                  Si tienes dudas, por favor contacta a soporte desde la aplicaci&oacute;n.
                </p>
                <p>
                  Saludos,<br/>
                  El equipo de %s
                </p>
              </body>
            </html>
            """.formatted(
                safeUserName,
                safeAppName,
                title != null ? title : "Aviso administrativo",
                mainMessage != null ? mainMessage : "",
                reasonBlock,
                typeBlock,
                priorityBlock,
                dateBlock,
                safeAppName
        );
    }

    // ============================================================
    //  EVENTOS DE VIAJE (Travel Events)
    // ============================================================

    //  VIAJE CREADO (TravelCreatedEvent)
    public String buildTravelCreatedEmail(
            String userName,
            String appName,
            String originName,
            String destinationName,
            String departureTimeFormatted,
            Integer availableSeats,
            Double pricePerSeat
    ) {
        String seatsText = (availableSeats != null)
                ? availableSeats + " cupos disponibles"
                : "Cupos disponibles según la ruta seleccionada";

        String priceText = (pricePerSeat != null)
                ? String.format("$%.2f por cupo", pricePerSeat)
                : "El precio se mostrará en la aplicación";

        return """
            <html>
              <body style="font-family: Arial, sans-serif;">
                <p>Hola %s,</p>

                <p>
                  Se ha creado un nuevo viaje en <strong>%s</strong> con la siguiente información:
                </p>

                <ul>
                  <li><strong>Origen:</strong> %s</li>
                  <li><strong>Destino:</strong> %s</li>
                  <li><strong>Hora de salida:</strong> %s</li>
                  <li><strong>Cupos:</strong> %s</li>
                  <li><strong>Precio:</strong> %s</li>
                </ul>

                <p>
                  Puedes ver más detalles y gestionar este viaje desde la aplicación.
                </p>

                <br/>
                <p>Saludos,<br/>El equipo de %s</p>
              </body>
            </html>
            """.formatted(
                userName,
                appName,
                originName,
                destinationName,
                departureTimeFormatted != null ? departureTimeFormatted : "Por definir",
                seatsText,
                priceText,
                appName
        );
    }

    //  VIAJE ACTUALIZADO (TravelUpdatedEvent)
    public String buildTravelUpdatedEmail(
            String userName,
            String appName,
            String travelId,
            String newStatus,
            String updateReason,
            String changesDescription
    ) {
        String reasonBlock = (updateReason != null && !updateReason.isBlank())
                ? "<p><strong>Motivo de la actualización:</strong> " + updateReason + "</p>"
                : "";

        String changesBlock = (changesDescription != null && !changesDescription.isBlank())
                ? "<p><strong>Cambios realizados:</strong><br/>" + changesDescription + "</p>"
                : "";

        return """
            <html>
              <body style="font-family: Arial, sans-serif;">
                <p>Hola %s,</p>

                <p>
                  Tu viaje con ID <strong>%s</strong> ha sido actualizado en <strong>%s</strong>.
                </p>

                <p>
                  <strong>Nuevo estado:</strong> %s
                </p>

                %s
                %s

                <p>
                  Te recomendamos revisar los detalles del viaje en la aplicación
                  para confirmar que todo esté en orden.
                </p>

                <br/>
                <p>Saludos,<br/>El equipo de %s</p>
              </body>
            </html>
            """.formatted(
                userName,
                travelId,
                appName,
                newStatus != null ? newStatus : "Actualizado",
                reasonBlock,
                changesBlock,
                appName
        );
    }

    //  VIAJE CANCELADO (TravelCancelledEvent)
    public String buildTravelCancelledEmail(
            String userName,
            String appName,
            String travelId,
            String originName,
            String destinationName,
            String cancellationReason,
            String cancelledBy,
            String refundPolicySummary
    ) {
        String reasonBlock = (cancellationReason != null && !cancellationReason.isBlank())
                ? "<p><strong>Motivo de la cancelación:</strong> " + cancellationReason + "</p>"
                : "<p>El viaje fue cancelado por el responsable.</p>";

        String cancelledByText = (cancelledBy != null && !cancelledBy.isBlank())
                ? switch (cancelledBy.toUpperCase()) {
            case "DRIVER" -> "el conductor";
            case "SYSTEM" -> "el sistema";
            case "ADMIN" -> "un administrador";
            default -> "el responsable";
        }
                : "el responsable";

        String refundBlock = (refundPolicySummary != null && !refundPolicySummary.isBlank())
                ? "<p><strong>Política de reembolso:</strong><br/>" + refundPolicySummary + "</p>"
                : "";

        return """
            <html>
              <body style="font-family: Arial, sans-serif;">
                <p>Hola %s,</p>

                <p>
                  Te informamos que tu viaje con ID <strong>%s</strong> ha sido cancelado en
                  <strong>%s</strong>.
                </p>

                <p>
                  <strong>Ruta:</strong> %s → %s
                </p>

                <p>
                  La cancelación fue realizada por %s.
                </p>

                %s
                %s

                <p>
                  Si tienes dudas adicionales, puedes comunicarte con nuestro equipo de soporte.
                </p>

                <br/>
                <p>Saludos,<br/>El equipo de %s</p>
              </body>
            </html>
            """.formatted(
                userName,
                travelId,
                appName,
                originName,
                destinationName,
                cancelledByText,
                reasonBlock,
                refundBlock,
                appName
        );
    }
}