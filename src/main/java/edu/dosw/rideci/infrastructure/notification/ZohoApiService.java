package edu.dosw.rideci.infrastructure.notification;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class ZohoApiService {

    private final ZohoTokenService tokenService;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${zoho.mail.api.url}")
    private String apiUrl;

    @Value("${zoho.mail.from-address}")
    private String fromAddress;

    // ID de cuenta. Si no lo tenemos, intentaremos obtenerlo o usar 'me' si la API
    // lo permite,
    // pero Zoho Mail API v2 suele usar {accountId}/messages.
    // Usaremos un AccountId hardcodeado temporalmente o lo buscaremos.
    // La mayoría de veces es el mismo UID del usuario principal.
    // Para simplificar, intentaremos listar cuentas primero si es necesario,
    // pero asumiremos que podemos usar el endpoint general o el ID del usuario.
    // NOTA: Para Zoho Mail API, la URL suele ser:
    // https://mail.zoho.com/api/accounts/{ACCOUNT_ID}/messages

    private String cachedAccountId;

    public void sendEmail(String to, String subject, String htmlContent) {
        String token = tokenService.getAccessToken();
        String accountId = getAccountId(token);

        String url = apiUrl + "/" + accountId + "/messages";

        ZohoEmailRequest emailRequest = new ZohoEmailRequest();
        emailRequest.setFromAddress(fromAddress);
        emailRequest.setToAddress(to);
        emailRequest.setSubject(subject);
        emailRequest.setContent(htmlContent);

        // Configurar HTML
        emailRequest.setHtmlMode(true);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ZohoEmailRequest> request = new HttpEntity<>(emailRequest, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
            log.info("Email enviado vía Zoho API a {}. Response: {}", to, response.getStatusCode());
        } catch (Exception e) {
            log.error("Error al enviar email vía Zoho API a {}", to, e);
            throw new RuntimeException("Fallo envío Zoho API", e);
        }
    }

    private String getAccountId(String token) {
        if (cachedAccountId != null)
            return cachedAccountId;

        try {
            // Intentamos obtener las cuentas
            String url = apiUrl; // https://mail.zoho.com/api/accounts
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity<Void> request = new HttpEntity<>(headers);

            ResponseEntity<ZohoAccountsResponse> response = restTemplate.exchange(
                    url, HttpMethod.GET, request, ZohoAccountsResponse.class);

            if (response.getBody() != null && response.getBody().getData() != null
                    && !response.getBody().getData().isEmpty()) {
                cachedAccountId = response.getBody().getData().get(0).getAccountId();
                log.info("Zoho Account ID obtenido: {}", cachedAccountId);
                return cachedAccountId;
            }
        } catch (Exception e) {
            log.warn("No se pudo obtener AccountID dinámicamente. Intentando usar ID de usuario del token o fallback.",
                    e);
        }

        // Fallback: Si falla, lanzamos error porque necesitamos el ID
        throw new RuntimeException("No se pudo resolver Zoho Account ID. Verifica credenciales.");
    }

    @Data
    private static class ZohoEmailRequest {
        @JsonProperty("fromAddress")
        private String fromAddress;

        @JsonProperty("toAddress")
        private String toAddress;

        @JsonProperty("subject")
        private String subject;

        @JsonProperty("content")
        private String content;

        @JsonProperty("html") // Algunos endpoints usan 'html' o query params. Ajustaremos estándar v2.
        private boolean htmlMode = true;

        // Nota: La API v2 de Zoho Mail usa JSON Body.
        // https://www.zoho.com/mail/help/api/post-send-an-email.html
        // La estructura real puede variar ligeramente.
        // A veces piden "content" y "contentType": "html"
        // Aseguramos compatibilidad básica.
    }

    @Data
    private static class ZohoAccountsResponse {
        @JsonProperty("data")
        private java.util.List<ZohoAccount> data;
    }

    @Data
    private static class ZohoAccount {
        @JsonProperty("accountId")
        private String accountId;

        @JsonProperty("incomingUserName")
        private String incomingUserName;
    }
}
