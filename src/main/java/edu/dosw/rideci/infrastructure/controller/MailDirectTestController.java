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

    @PostMapping
    public ResponseEntity<Void> sendTestMail() {
        String to = "raquelselma96@gmail.com";
        log.info(">>> Enviando correo de prueba a {}", to);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Prueba SMTP Office365 - ATENEA");
        message.setText("Hola, este es un correo de prueba enviado usando SMTP de Office365 desde ATENEA_NOTIFICATIONS_BACKEND.");

        message.setFrom("raquel.selma-a@mail.escuelaing.edu.co");

        mailSender.send(message);
        log.info(">>> Correo enviado sin excepciones");

        return ResponseEntity.ok().build();
    }
}
