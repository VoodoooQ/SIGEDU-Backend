package com.gestion.educativa.calendario.calendario.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI calendarioOpenApi() {
		return new OpenAPI()
				.info(new Info()
						.title("MS Calendario SIGEDU")
						.description("API para gestion de eventos y calendario institucional")
						.version("1.0.0"))
				.addServersItem(new Server()
						.url("http://localhost:8084")
						.description("Calendario local"));
	}
}
