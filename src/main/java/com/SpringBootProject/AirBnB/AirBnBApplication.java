package com.SpringBootProject.AirBnB;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling

public class AirBnBApplication {

	public static void main(String[] args) {
		SpringApplication.run(AirBnBApplication.class, args);
	}

}
