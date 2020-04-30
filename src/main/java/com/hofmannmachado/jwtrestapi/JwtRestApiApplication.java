package com.hofmannmachado.jwtrestapi;

import com.hofmannmachado.jwtrestapi.api.dtos.FuncionarioDto;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class JwtRestApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(JwtRestApiApplication.class, args);
	}

}
