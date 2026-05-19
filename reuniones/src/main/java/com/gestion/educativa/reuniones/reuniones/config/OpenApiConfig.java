package com.gestion.educativa.reuniones.reuniones.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI reunionesOpenApi() {
		return new OpenAPI()
				.info(new Info()
						.title("MS Reuniones SIGEDU")
						.description("API para reuniones y acuerdos institucionales")
						.version("1.0.0"))
				.addServersItem(new Server()
						.url("http://localhost:8082")
						.description("Reuniones local"));
	}
}
