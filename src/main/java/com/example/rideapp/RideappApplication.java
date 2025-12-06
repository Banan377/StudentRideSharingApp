package com.example.rideapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class RideappApplication {

	public static void main(String[] args) {
		SpringApplication.run(RideappApplication.class, args);
	}

}
