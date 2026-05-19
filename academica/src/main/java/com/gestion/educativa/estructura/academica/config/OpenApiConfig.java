package com.gestion.educativa.estructura.academica.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI academicaOpenApi() {
		return new OpenAPI()
				.info(new Info()
						.title("MS Academica SIGEDU")
						.description("API para gestion de cursos, niveles, periodos y salas")
						.version("1.0.0"))
				.addServersItem(new Server()
						.url("http://localhost:8083")
						.description("Academica local"));
	}
}
