package com.gestion.educativa.notas.notas.config;

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
    public OpenAPI notasOpenApi() {
        String nombreEsquema = "bearerAuth";
        return new OpenAPI()
                .info(new Info()
                        .title("MS6 - Notas SIGEDU")
                        .description("API para registro y gesti?n de notas")
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
                        .url("http://localhost:8090")
                        .description("MS6 local"));
    }
}
