package br.com.claudemirojr.trade.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI customOpenApi() {
		// http://localhost:8080/v3/api-docs
		// http://localhost:8080/swagger-ui/index.html
		
		return new OpenAPI()
		        .info(new Info()
		                .title("RESTfull API Trade Esportivo")
		                .version("v1")
		                .description("Disponibilizar CRUD de trade esportivo")
		                .termsOfService("http://swagger.io/terms/")
		                .license(new License().name("Apache 2.0").url("http://springdoc.org"))
		                .contact(new Contact().name("Claudemiro Jr").email("volverinejr@gmail.com").url("https://www.linkedin.com/in/claudemirojr/"))
		        		);
	}
}
