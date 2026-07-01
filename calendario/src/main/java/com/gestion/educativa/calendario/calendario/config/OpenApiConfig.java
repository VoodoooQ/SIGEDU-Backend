package com.gestion.educativa.calendario.calendario.config;

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
    public OpenAPI calendarioOpenApi() {
        String nombreEsquema = "bearerAuth";
        return new OpenAPI()
                .info(new Info()
                        .title("MS8 - Calendario SIGEDU")
                        .description("API para gesti?n de eventos y calendario institucional")
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
                        .url("http://localhost:8084")
                        .description("MS8 local"));
    }
}
