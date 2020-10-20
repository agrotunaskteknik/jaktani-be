package com.cartas.jaktani;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ConfigurationPropertiesScan("com.cartas.jaktani.util")
public class JaktaniApplication {

	public static void main(String[] args) {
		SpringApplication.run(JaktaniApplication.class, args);
	}

}
