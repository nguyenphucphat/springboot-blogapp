package com.springboot.blog;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Spring Boot Blog Application REST API",
				version = "1.0",
				description = "Spring Boot Blog Application REST API Documentation",
				contact = @Contact(
						name = "Phat Nguyen",
						email = "nguyenphucphat111999@gmail.com",
						url = "https://www.facebook.com/conchimnon.npp/")
		),
		servers = {
				@Server(
						url = "http://localhost:8080",
						description = "Local server"
				),
				@Server(
						url = "None",
						description = "Aws server"
				)
		},
		externalDocs = @ExternalDocumentation(
				description = "Spring Boot Blog Application REST API Source Code",
				url = "https://github.com/nguyenphucphat/springboot-blogapp"
		)
)
public class SpringbootBlogApplicationRestApiApplication{

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringbootBlogApplicationRestApiApplication.class, args);
	}

}
