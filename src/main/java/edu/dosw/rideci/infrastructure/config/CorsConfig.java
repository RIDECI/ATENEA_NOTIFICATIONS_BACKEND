package edu.dosw.rideci.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;

/**
 * Configuración de CORS (Cross-Origin Resource Sharing).
 * Los orígenes permitidos se configuran mediante variables de entorno
 * para mayor seguridad y flexibilidad entre ambientes.
 */
@Configuration
public class CorsConfig {

    @Value("${cors.allowed-origins:http://localhost:3000,http://localhost:4200}")
    private String allowedOrigins;

    @Value("${cors.allowed-methods:GET,POST,PUT,DELETE,PATCH,OPTIONS}")
    private String allowedMethods;

    @Value("${cors.allowed-headers:*}")
    private String allowedHeaders;

    @Value("${cors.allow-credentials:true}")
    private boolean allowCredentials;

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        // Configurar credenciales
        config.setAllowCredentials(allowCredentials);

        // Configurar orígenes permitidos desde variable de entorno
        List<String> origins = Arrays.asList(allowedOrigins.split(","));
        config.setAllowedOriginPatterns(origins);

        // Configurar headers permitidos
        config.setAllowedHeaders(Arrays.asList(allowedHeaders.split(",")));

        // Configurar métodos HTTP permitidos
        config.setAllowedMethods(Arrays.asList(allowedMethods.split(",")));

        // Exponer headers en la respuesta
        config.setExposedHeaders(Arrays.asList("Authorization", "Content-Type"));

        // Aplicar la configuración a todos los endpoints
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}
