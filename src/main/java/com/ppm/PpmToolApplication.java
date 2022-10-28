package com.ppm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan
public class PpmToolApplication {

	public static void main(String[] args) {
		SpringApplication.run(PpmToolApplication.class, args);
	}

}
