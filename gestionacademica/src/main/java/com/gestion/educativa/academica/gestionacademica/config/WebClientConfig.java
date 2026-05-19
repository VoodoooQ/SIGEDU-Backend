package com.gestion.educativa.academica.gestionacademica.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${microservices.academica.url}")
    private String academicaBaseUrl;

    @Value("${microservices.identidad.url}")
    private String identidadBaseUrl;

    @Bean
    public WebClient academicaWebClient() {
        return WebClient.builder().baseUrl(academicaBaseUrl).build();
    }

    @Bean
    public WebClient identidadWebClient() {
        return WebClient.builder().baseUrl(identidadBaseUrl).build();
    }
}
