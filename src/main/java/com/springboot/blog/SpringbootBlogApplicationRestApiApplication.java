package com.springboot.blog;

import com.springboot.blog.entity.Post;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringbootBlogApplicationRestApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootBlogApplicationRestApiApplication.class, args);
	}

}
