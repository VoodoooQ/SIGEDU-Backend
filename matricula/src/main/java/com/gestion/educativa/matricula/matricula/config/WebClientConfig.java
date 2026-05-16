package com.gestion.educativa.matricula.matricula.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
@Configuration
public class WebClientConfig {
    @Bean
    //añadir puerto de estructura academica 
    public WebClient academicaWebClient(){
        return WebClient.builder().baseUrl("http://127.0.0.1:").build();
    }
    //Añadir puerto de identidad
    @Bean
    public WebClient identidadWebClient(){
        return WebClient.builder().baseUrl("http://127.0.0.1:").build();
    }
}
