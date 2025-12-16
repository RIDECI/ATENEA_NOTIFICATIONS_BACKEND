package edu.dosw.rideci.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.Collections;

/**
 * Configuración de CORS (Cross-Origin Resource Sharing) para permitir
 * peticiones desde cualquier origen.
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        // Permitir credenciales (cookies, headers de autenticación)
        config.setAllowCredentials(true);

        // Permitir todos los orígenes
        config.setAllowedOriginPatterns(Collections.singletonList("*"));

        // Permitir todos los headers
        config.setAllowedHeaders(Collections.singletonList("*"));

        // Permitir todos los métodos HTTP (GET, POST, PUT, DELETE, etc.)
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));

        // Exponer todos los headers en la respuesta
        config.setExposedHeaders(Arrays.asList("Authorization", "Content-Type"));

        // Aplicar la configuración a todos los endpoints
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}
