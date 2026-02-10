package com.example.desafioCadastro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class GestaoPetApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestaoPetApiApplication.class, args);
	}

}
