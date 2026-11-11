package com.github.s0phs.ms_doacoes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsDoacoesApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsDoacoesApplication.class, args);
	}

}
