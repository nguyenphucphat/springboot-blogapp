package com.springboot.blog;

import com.springboot.blog.entity.Post;
import com.springboot.blog.entity.Role;
import com.springboot.blog.entity.User;
import com.springboot.blog.repository.RoleRepository;
import com.springboot.blog.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@SpringBootApplication
public class SpringbootBlogApplicationRestApiApplication{

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(SpringbootBlogApplicationRestApiApplication.class, args);
	}

}
