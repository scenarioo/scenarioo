package org.scenarioo;

import org.scenarioo.rest.application.ScenariooWebApplication;
import org.scenarioo.rest.base.logging.RequestLogggingFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class ScenariooViewerApplication implements WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(ScenariooViewerApplication.class, args);
	}

	@Bean
	public ScenariooWebApplication initializeApplication() {
		return new ScenariooWebApplication();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new RequestLogggingFilter());
	}
}
