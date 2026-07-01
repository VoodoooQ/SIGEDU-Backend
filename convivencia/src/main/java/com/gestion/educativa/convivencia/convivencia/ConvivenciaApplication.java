package com.gestion.educativa.convivencia.convivencia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class ConvivenciaApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConvivenciaApplication.class, args);
    }

    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
                registry.addRedirectViewController("/", "/swagger-ui/index.html");
                registry.addRedirectViewController("/swagger-ui", "/swagger-ui/index.html");
            }
        };
    }
}
