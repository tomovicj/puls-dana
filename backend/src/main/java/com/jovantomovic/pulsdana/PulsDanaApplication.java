package com.jovantomovic.pulsdana;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PulsDanaApplication {

	public static void main(String[] args) {
		SpringApplication.run(PulsDanaApplication.class, args);
	}

}
