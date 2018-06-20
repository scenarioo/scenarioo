package org.scenarioo;

import org.scenarioo.rest.application.ScenariooWebApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ScenariooViewerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScenariooViewerApplication.class, args);
	}

	@Bean
	public ScenariooWebApplication initializeApplication() {
		return new ScenariooWebApplication();
	}
}
