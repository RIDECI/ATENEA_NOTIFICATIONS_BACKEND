package edu.dosw.rideci.infrastructure.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/test/mail")
@RequiredArgsConstructor
public class MailDirectTestController {

    private final JavaMailSender mailSender;

    // ============================
    // PRUEBA COMPLETA CON DEBUG
    // ============================
    @PostMapping("/debug-full")
    public ResponseEntity<String> sendDebugFull() {

        String to = "juanpablonietocortes32@gmail.com";
        String from = "rideci-email@rideci.online";

        log.info(">>> [ZOHO-DEBUG] Iniciando prueba COMPLETA Zoho SMTP");
        log.info(">>> Destinatario: {}", to);
        log.info(">>> Remitente: {}", from);

        try {
            // Mostrar configuración actual del SMTP cargado por Spring
            org.springframework.mail.javamail.JavaMailSenderImpl mailSenderImpl =
                    (org.springframework.mail.javamail.JavaMailSenderImpl) mailSender;

            log.info(">>> Configuración SMTP actual:");
            log.info(">>>   Host: {}", mailSenderImpl.getHost());
            log.info(">>>   Port: {}", mailSenderImpl.getPort());
            log.info(">>>   Username: {}", mailSenderImpl.getUsername());
            log.info(">>>   Protocol: {}", mailSenderImpl.getProtocol());
            log.info(">>>   JavaMail Properties: {}", mailSenderImpl.getJavaMailProperties());

            // Construir correo
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setFrom(from);
            message.setSubject("[DEBUG] Prueba Zoho SMTP - " + System.currentTimeMillis());
            message.setText(
                    "Prueba completa de Zoho SMTP\n\n" +
                            "Timestamp: " + java.time.Instant.now() + "\n" +
                            "From: " + from + "\n" +
                            "To: " + to + "\n" +
                            "Este es un email de prueba para debug."
            );

            log.info(">>> [ZOHO-DEBUG] Enviando email...");
            mailSender.send(message);
            log.info(">>> [ZOHO-DEBUG] Email enviado SIN errores");

            return ResponseEntity.ok(
                    "✅ Email enviado correctamente.\n" +
                            "Revisa tu correo: " + to
            );

        } catch (Exception e) {
            log.error(">>> [ZOHO-DEBUG] ERROR COMPLETO:", e);
            return ResponseEntity.status(500)
                    .body("❌ Error al enviar correo:\n" +
                            "Clase: " + e.getClass().getName() + "\n" +
                            "Mensaje: " + e.getMessage());
        }
    }


    // ============================
    // PRUEBA SIMPLE
    // ============================
    @PostMapping
    public ResponseEntity<String> sendTestMail() {

        String to = "juanpablonietocortes32@gmail.com";
        String from = "rideci-email@rideci.online";

        log.info(">>> Enviando correo de prueba a {}", to);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setFrom(from);
        message.setSubject("🚀 Zoho SMTP funcionando - ATENEA");
        message.setText(
                "Hola Juan,\n\n" +
                        "Zoho SMTP está funcionando correctamente desde tu backend ATENEA.\n\n" +
                        "Detalles:\n" +
                        "- Remitente: " + from + "\n" +
                        "- Destinatario: " + to + "\n\n" +
                        "Todo listo para enviar notificaciones reales.\n\n" +
                        "Saludos,\nATENEA Notifications"
        );

        try {
            mailSender.send(message);
            log.info(">>> Correo enviado exitosamente");
            return ResponseEntity.ok("✅ Correo enviado a " + to);
        } catch (Exception e) {
            log.error(">>> Error: {}", e.getMessage(), e);
            return ResponseEntity.status(500)
                    .body("❌ Error enviando correo: " + e.getMessage());
        }
    }

}
