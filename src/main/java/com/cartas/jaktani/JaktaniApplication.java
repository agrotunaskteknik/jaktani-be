package com.cartas.jaktani;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan("com.cartas.jaktani.util")
public class JaktaniApplication {

	public static void main(String[] args) {
		SpringApplication.run(JaktaniApplication.class, args);
	}
	 
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(JaktaniApplication.class);

    }
	

}
