package com.hackaboss.travelagency;

import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import io.swagger.v3.oas.models.info.Info;
@SpringBootApplication
public class TravelagencyApplication {

	public static void main(String[] args) {
		SpringApplication.run(TravelagencyApplication.class, args);
	}

	@Bean
	public OpenAPI info() {
		return new OpenAPI()
				.info(new Info().title("Agencia de Turismo - API REST")
						.description("API para gestionar una agencia de turismo")
						.contact(new io.swagger.v3.oas.models.info.Contact().email("albertoarroyo@hotmail.es"))
						.version("1.0"));
	}

}
