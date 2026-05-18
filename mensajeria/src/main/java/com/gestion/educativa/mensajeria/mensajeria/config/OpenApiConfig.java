package com.gestion.educativa.mensajeria.mensajeria.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI mensajeriaOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("MS5 - Comunicaciones Internas SIGEDU")
                        .description("API para mensajeria interna entre usuarios")
                        .version("1.0.0"))
                .addServersItem(new Server()
                        .url("http://localhost:8089")
                        .description("Mensajeria local"));
    }
}
