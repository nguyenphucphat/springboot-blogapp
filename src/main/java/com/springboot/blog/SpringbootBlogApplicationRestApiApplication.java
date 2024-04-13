package com.springboot.blog;

import com.springboot.blog.entity.Role;
import com.springboot.blog.entity.User;
import com.springboot.blog.repository.RoleRepository;
import com.springboot.blog.repository.UserRepository;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

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
						url = "http://blog.ap-southeast-2.elasticbeanstalk.com/",
						description = "Aws Elastic Beanstalk server"
				)
		},
		externalDocs = @ExternalDocumentation(
				description = "Spring Boot Blog Application REST API Source Code",
				url = "https://github.com/nguyenphucphat/springboot-blogapp"
		)
)
public class SpringbootBlogApplicationRestApiApplication implements CommandLineRunner {

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(SpringbootBlogApplicationRestApiApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		if(roleRepository.findByName("ROLE_ADMIN").isEmpty()) {
			Role roleAdmin = new Role();
			roleAdmin.setName("ROLE_ADMIN");

			roleRepository.save(roleAdmin);
		}

		if(roleRepository.findByName("ROLE_USER").isEmpty()) {
			Role roleUser = new Role();
			roleUser.setName("ROLE_USER");

			roleRepository.save(roleUser);
		}

		if(userRepository.findByUsername("admin").isEmpty()) {
			User admin = new User();
			admin.setUsername("admin");
			admin.setPassword(passwordEncoder.encode("admin"));
			admin.setEmail("admin@gmail.com");
			admin.setName("Admin");

			Role savedAdminRole = roleRepository.findByName("ROLE_ADMIN").get();
			admin.getRoles().add(savedAdminRole);

			Role savedUserRole = roleRepository.findByName("ROLE_USER").get();
			admin.getRoles().add(savedUserRole);

			userRepository.save(admin);
		}
	}
}
