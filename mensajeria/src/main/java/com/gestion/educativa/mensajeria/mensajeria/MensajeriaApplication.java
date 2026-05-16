package com.gestion.educativa.mensajeria.mensajeria;

import com.gestion.educativa.mensajeria.mensajeria.config.SecurityInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class MensajeriaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MensajeriaApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer webMvcConfigurer(SecurityInterceptor securityInterceptor) {
		return new WebMvcConfigurer() {
			@Override
			public void addInterceptors(InterceptorRegistry registry) {
				registry.addInterceptor(securityInterceptor)
						.addPathPatterns("/api/**")
						.excludePathPatterns("/swagger-ui/**", "/v3/api-docs/**");
			}
		};
	}
}
