package com.example.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GymBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(GymBackendApplication.class, args);
	}

}
