package edu.dosw.rideci.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${spring.application.name:ATENEA_NOTIFICATIONS_BACKEND}")
    private String appName;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(appName)
                        .version("1.0.0")
                        .description("API Documentation for Atenea Notifications Backend"))
                .servers(List.of(
                        new Server().url("https://ateneanotificationsbackend-production.up.railway.app")
                                .description("Production Server (HTTPS)"),
                        new Server().url("http://localhost:8080").description("Local Development")));
    }
}
