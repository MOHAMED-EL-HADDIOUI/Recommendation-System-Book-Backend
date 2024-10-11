package com.mohamedelhaddioui.Recommendation.System.Book.security.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        System.out.println("--------------------- CorsRegistry ---------------------");
        registry.addMapping("/**") // Appliquer CORS à tous les points de terminaison
                .allowedOrigins("http://localhost:4200") // Autoriser l'origine localhost:4200
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Autoriser ces méthodes HTTP
                .allowedHeaders("*") // Autoriser tous les en-têtes
                .allowCredentials(true); // Autoriser les cookies et autres informations d'identification
    }
}
