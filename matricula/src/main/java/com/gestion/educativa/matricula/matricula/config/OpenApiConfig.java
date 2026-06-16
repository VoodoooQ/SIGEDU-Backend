package com.gestion.educativa.matricula.matricula.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI matriculaOpenApi() {
        String nombreEsquema = "bearerAuth";
        return new OpenAPI()
                .info(new Info()
                        .title("MS3 - Matr?cula SIGEDU")
                        .description("API para gesti?n de matr?culas y antecedentes")
                        .version("1.0.0"))
                .components(new Components().addSecuritySchemes(
                        nombreEsquema,
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                ))
                .addSecurityItem(new SecurityRequirement().addList(nombreEsquema))
                .addServersItem(new Server()
                        .url("http://localhost:8088")
                        .description("MS3 local"));
    }
}
