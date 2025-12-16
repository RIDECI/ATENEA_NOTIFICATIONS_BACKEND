package edu.dosw.rideci.infrastructure.notification;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ZohoTokenService {

    @Value("${zoho.mail.auth.url}")
    private String authUrl;

    @Value("${zoho.mail.client-id}")
    private String clientId;

    @Value("${zoho.mail.client-secret}")
    private String clientSecret;

    @Value("${zoho.mail.refresh-token}")
    private String refreshToken;

    private String accessToken;
    private LocalDateTime tokenExpiry;

    private final RestTemplate restTemplate = new RestTemplate();

    public synchronized String getAccessToken() {
        if (accessToken == null || isTokenExpired()) {
            refreshAccessToken();
        }
        return accessToken;
    }

    private boolean isTokenExpired() {
        // Renovar si quedan menos de 5 minutos
        return tokenExpiry == null || LocalDateTime.now().isAfter(tokenExpiry.minusMinutes(5));
    }

    private void refreshAccessToken() {
        try {
            log.info("Refrescando token de acceso de Zoho...");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("refresh_token", refreshToken);
            map.add("client_id", clientId);
            map.add("client_secret", clientSecret);
            map.add("grant_type", "refresh_token");

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

            ResponseEntity<ZohoTokenResponse> response = restTemplate.postForEntity(
                    authUrl,
                    request,
                    ZohoTokenResponse.class);

            if (response.getBody() != null && response.getBody().getAccessToken() != null) {
                this.accessToken = response.getBody().getAccessToken();
                // Los tokens suelen durar 1 hora (3600 seg)
                long expiresInSeconds = response.getBody().getExpiresIn() > 0 ? response.getBody().getExpiresIn()
                        : 3600;
                this.tokenExpiry = LocalDateTime.now().plusSeconds(expiresInSeconds);
                log.info("Token de Zoho renovado exitosamente. Expira en {} segundos.", expiresInSeconds);
            } else {
                log.error("Respuesta vacía al refrescar token de Zoho: {}", response);
                throw new RuntimeException("No se pudo obtener Access Token de Zoho");
            }

        } catch (Exception e) {
            log.error("Error crítico al refrescar token de Zoho: {}", e.getMessage());
            throw new RuntimeException("Fallo en autenticación con Zoho API", e);
        }
    }

    @Data
    private static class ZohoTokenResponse {
        @JsonProperty("access_token")
        private String accessToken;

        @JsonProperty("expires_in")
        private long expiresIn;

        @JsonProperty("token_type")
        private String tokenType;

        @JsonProperty("error")
        private String error;
    }
}
