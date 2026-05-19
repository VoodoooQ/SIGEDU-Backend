package com.gestion.educativa.convivencia.convivencia.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI convivenciaOpenApi() {
		return new OpenAPI()
				.info(new Info()
						.title("MS Convivencia SIGEDU")
						.description("API para hoja de vida y anotaciones de convivencia escolar")
						.version("1.0.0"))
				.addServersItem(new Server()
						.url("http://localhost:8085")
						.description("Convivencia local"));
	}
}
