package com.gestion.educativa.notas.notas.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI notasOpenApi() {
		return new OpenAPI()
				.info(new Info()
						.title("MS Notas SIGEDU")
						.description("API para registro y gestion de notas")
						.version("1.0.0"))
				.addServersItem(new Server()
						.url("http://localhost:8090")
						.description("Notas local"));
	}
}
