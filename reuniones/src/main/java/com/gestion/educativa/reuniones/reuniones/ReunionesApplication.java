package com.gestion.educativa.reuniones.reuniones;

import com.gestion.educativa.reuniones.reuniones.config.SecurityInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class ReunionesApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReunionesApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer webMvcConfigurer(SecurityInterceptor securityInterceptor) {
		return new WebMvcConfigurer() {
			@Override
			public void addInterceptors(InterceptorRegistry registry) {
				registry.addInterceptor(securityInterceptor)
						.addPathPatterns("/api/**");
			}
		};
	}

}
