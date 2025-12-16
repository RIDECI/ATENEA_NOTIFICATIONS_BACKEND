package edu.dosw.rideci.infrastructure.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth")
public class OAuthCallbackController {

    @GetMapping("/callback")
    public ResponseEntity<String> callback(@RequestParam("code") String code) {
        return ResponseEntity.ok("<h1>CODE RECIBIDO DE ZOHO:</h1><p>" + code
                + "</p><p>Copia este código y úsalo para generar el Refresh Token.</p>");
    }
}
