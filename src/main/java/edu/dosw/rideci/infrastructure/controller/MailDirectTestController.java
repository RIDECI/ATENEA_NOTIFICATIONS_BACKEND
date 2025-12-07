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

    @PostMapping("/debug-full")
    public ResponseEntity<String> sendDebugFull() {
        String to = "juanandrade.jaja@gmail.com";
        String from = "ridecicvdsdosw@gmail.com";

        log.info(">>> [DEBUG-FULL] Iniciando prueba COMPLETA de Mailjet");
        log.info(">>> Destinatario: {}", to);
        log.info(">>> Remitente: {}", from);

        try {
            // 1. Obtener configuración actual
            org.springframework.mail.javamail.JavaMailSenderImpl mailSenderImpl =
                    (org.springframework.mail.javamail.JavaMailSenderImpl) mailSender;

            log.info(">>> Configuración SMTP actual:");
            log.info(">>>   Host: {}", mailSenderImpl.getHost());
            log.info(">>>   Port: {}", mailSenderImpl.getPort());
            log.info(">>>   Username: {}", mailSenderImpl.getUsername());
            log.info(">>>   Protocol: {}", mailSenderImpl.getProtocol());
            log.info(">>>   JavaMail Properties: {}", mailSenderImpl.getJavaMailProperties());

            // 2. Enviar email con try-catch detallado
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("[DEBUG] Prueba Mailjet - " + System.currentTimeMillis());
            message.setText(
                    "DEBUG EMAIL\n\n" +
                            "Timestamp: " + java.time.Instant.now() + "\n" +
                            "From: " + from + "\n" +
                            "To: " + to + "\n" +
                            "API Key: bad07fb30f57586ed3f33c44f1c18538\n" +
                            "Este es un email de prueba para debug."
            );
            message.setFrom(from);

            log.info(">>> [DEBUG-FULL] Intentando enviar email...");
            mailSender.send(message);
            log.info(">>> [DEBUG-FULL] mailSender.send() completado sin excepciones");

            return ResponseEntity.ok(
                    "✅ mailSender.send() completado sin excepciones.\n" +
                            "Revisa los logs DEBUG para ver la conversación SMTP.\n" +
                            "Luego ve a Mailjet Dashboard → Transactional → Emails para ver el estado."
            );

        } catch (Exception e) {
            log.error(">>> [DEBUG-FULL] ERROR COMPLETO:", e);
            return ResponseEntity.status(500)
                    .body("❌ Error completo:\n" +
                            "Clase: " + e.getClass().getName() + "\n" +
                            "Mensaje: " + e.getMessage() + "\n" +
                            "Causa: " + (e.getCause() != null ? e.getCause().getMessage() : "null"));
        }
    }

    @PostMapping
    public ResponseEntity<String> sendTestMail() {
        String to = "juanandrade.jaja@gmail.com";
        log.info(">>> Enviando correo de prueba a {} usando Mailjet", to);
        log.info(">>> Remitente VERIFICADO: ridecicvdsdosw@gmail.com");

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("🚀 Mailjet Configurado - ATENEA");
        message.setText(
                "Hola Juan,\n\n" +
                        "¡Excelente! Has configurado Mailjet correctamente.\n\n" +
                        "Detalles:\n" +
                        "- Remitente: ridecicvdsdosw@gmail.com\n" +
                        "- API Key: bad07fb30f57586ed3f33c44f1c18538\n" +
                        "- Aplicación: ATENEA_NOTIFICATIONS_BACKEND\n\n" +
                        "Ahora puedes desplegar tu aplicación en cualquier servidor.\n\n" +
                        "Saludos,\nEquipo ATENEA"
        );

        // USAR EL EMAIL VERIFICADO EN MAILJET
        message.setFrom("ridecicvdsdosw@gmail.com");

        try {
            mailSender.send(message);
            log.info(">>> Correo enviado exitosamente");
            return ResponseEntity.ok("✅ Correo enviado a juanandrade.jaja@gmail.com desde ridecicvdsdosw@gmail.com");
        } catch (Exception e) {
            log.error(">>> Error: {}", e.getMessage(), e);
            return ResponseEntity.status(500)
                    .body("❌ Error: " + e.getMessage());
        }
    }
}