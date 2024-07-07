package br.com.openlabs.home_assistant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HomeAssistantApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomeAssistantApplication.class, args);
	}

}
